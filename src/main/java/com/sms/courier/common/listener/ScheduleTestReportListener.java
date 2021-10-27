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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            NoticeType noticeType = scheduleEntity.getNoticeType();
            switch (noticeType) {
                case ALL:
                    sendEmail(scheduleEntity, event);
                    break;
                case SUCCESS:
                    if (jobReport.getJobStatus() == JobStatus.SUCCESS) {
                        sendEmail(scheduleEntity, event);
                    }
                    break;
                case FAIL:
                    if (jobReport.getJobStatus() == JobStatus.FAIL) {
                        sendEmail(scheduleEntity, event);
                    }
                    break;
                default:
                    break;
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
        if (CollectionUtils.isNotEmpty(scheduleEntity.getEmails())) {
            TestReportEmailModelBuilder testReportEmailModelBuilder = TestReportEmailModel.builder()
                .delayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost())
                .paramsTotalTimeCost(jobReport.getParamsTotalTimeCost())
                .totalTimeCost(jobReport.getTotalTimeCost())
                .projectId(scheduleEntity.getProjectId())
                .name(scheduleEntity.getName() + "_" + event.getName());
            if (jobReport instanceof SceneCaseJobReport) {
                SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
                List<CaseReport> caseReportList = sceneCaseJobReport.getCaseReportList();
                if (CollectionUtils.isEmpty(caseReportList)) {
                    testReportEmailModelBuilder.success(0);
                    testReportEmailModelBuilder.fail(count);
                } else {
                    int success =
                        (int) caseReportList.stream().filter(report -> ResultType.SUCCESS == report.getIsSuccess())
                            .count();
                    testReportEmailModelBuilder.success(success);
                    testReportEmailModelBuilder.fail(count - success);
                }
            } else {
                testReportEmailModelBuilder.success(jobReport.getJobStatus() == JobStatus.SUCCESS ? 1 : 0);
                testReportEmailModelBuilder.fail(jobReport.getJobStatus() == JobStatus.FAIL ? 1 : 0);
            }
            TestReportEmailModel testReportEmailModel = testReportEmailModelBuilder.build();
            Map<AdditionalParam, Object> additionalParam = new HashMap<>();
            additionalParam.put(AdditionalParam.EMAIL_TO, scheduleEntity.getEmails());
            NotificationPayload notificationPayload = NotificationPayload.builder()
                .contentVariable(testReportEmailModel)
                .titleVariable(testReportEmailModel).additionalParam(additionalParam).build();
            sender.sendTestReportNotification(notificationPayload);
        }
    }

}
