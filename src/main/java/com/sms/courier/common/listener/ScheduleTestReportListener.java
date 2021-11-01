package com.sms.courier.common.listener;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.modal.TestReportEmailModel;
import com.sms.courier.chat.modal.TestReportEmailModel.TestReportEmailModelBuilder;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.listener.event.ScheduleTestReportEvent;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTestReportListener {

    private final CommonRepository commonRepository;
    private final Sender sender;

    public ScheduleTestReportListener(CommonRepository commonRepository, Sender sender) {
        this.commonRepository = commonRepository;
        this.sender = sender;
    }

    @EventListener
    public void doProcess(ScheduleTestReportEvent event) {
        try {
            JobReport jobReport = event.getJobReport();
            ScheduleRecordEntity scheduleRecord = commonRepository.findById(event.getId(), ScheduleRecordEntity.class)
                .orElseThrow(() -> ExceptionUtils.mpe("The ScheduleRecord not exist! id = %s", event.getId()));
            ScheduleEntity scheduleEntity = commonRepository
                .findById(scheduleRecord.getScheduleId(), ScheduleEntity.class)
                .orElseThrow(
                    () -> ExceptionUtils.mpe("The Schedule not exist! name = %s", scheduleRecord.getScheduleName()));
            NoticeType noticeType = Objects.requireNonNullElse(scheduleEntity.getNoticeType(), NoticeType.CLOSE);
            if (noticeType == NoticeType.ALL) {
                sendEmail(scheduleEntity, event);
            }
            if (noticeType == NoticeType.SUCCESS && jobReport.getJobStatus() == JobStatus.SUCCESS) {
                sendEmail(scheduleEntity, event);
            }
            if (noticeType == NoticeType.FAIL && jobReport.getJobStatus() == JobStatus.FAIL) {
                sendEmail(scheduleEntity, event);
            }
        } catch (ApiTestPlatformException e) {
            log.error("Schedule test report custom error", e);
        } catch (Exception e) {
            log.error("Schedule test report system error!", e);
        }
    }

    private void sendEmail(ScheduleEntity scheduleEntity, ScheduleTestReportEvent event) {
        JobReport jobReport = event.getJobReport();
        int count = event.getCount();
        log.info("Send test report email to {}", scheduleEntity.getEmails());
        if (CollectionUtils.isEmpty(scheduleEntity.getEmails())) {
            return;
        }
        TestReportEmailModel testReportEmailModel = buildTestReportEmailModel(jobReport, count);
        testReportEmailModel.setCaseName(event.getName());
        testReportEmailModel.setScheduleName(scheduleEntity.getName());
        testReportEmailModel.setDataName(event.getDataName());
        testReportEmailModel.setProjectId(scheduleEntity.getProjectId());
        Map<AdditionalParam, Object> additionalParam = new HashMap<>();
        additionalParam.put(AdditionalParam.EMAIL_TO, scheduleEntity.getEmails());
        NotificationPayload notificationPayload = NotificationPayload.builder()
            .contentVariable(testReportEmailModel)
            .titleVariable(testReportEmailModel).additionalParam(additionalParam).build();
        sender.sendTestReportNotification(notificationPayload);
    }

    private TestReportEmailModel buildTestReportEmailModel(JobReport jobReport, int count) {
        TestReportEmailModelBuilder testReportEmailModelBuilder = TestReportEmailModel.builder()
            .delayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost())
            .paramsTotalTimeCost(jobReport.getParamsTotalTimeCost())
            .totalTimeCost(jobReport.getTotalTimeCost());
        if (jobReport instanceof SceneCaseJobReport) {
            SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
            List<CaseReport> caseReportList = Objects.requireNonNullElse(sceneCaseJobReport.getCaseReportList(),
                Collections.emptyList());
            long success = caseReportList.stream().filter(report -> ResultType.SUCCESS == report.getIsSuccess())
                .count();
            testReportEmailModelBuilder.success(success);
            testReportEmailModelBuilder.fail(count - success);
        } else {
            testReportEmailModelBuilder.success(jobReport.getJobStatus() == JobStatus.SUCCESS ? 1L : 0L);
            testReportEmailModelBuilder.fail(jobReport.getJobStatus() == JobStatus.FAIL ? 1L : 0L);
        }
        return testReportEmailModelBuilder.build();
    }

}
