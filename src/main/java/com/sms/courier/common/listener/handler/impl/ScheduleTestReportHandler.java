package com.sms.courier.common.listener.handler.impl;

import com.sms.courier.chat.modal.TestReportEmailModel;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.listener.event.TestReportEvent;
import com.sms.courier.common.listener.handler.TestReportHandleType;
import com.sms.courier.common.listener.handler.enums.TestReportType;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@TestReportHandleType(type = TestReportType.SCHEDULER)
public class ScheduleTestReportHandler extends AbstractTestReportHandler {

    private final CommonRepository commonRepository;

    public ScheduleTestReportHandler(Sender sender, CommonRepository commonRepository) {
        super(sender);
        this.commonRepository = commonRepository;
    }

    @Override
    public void handle(TestReportEvent event) {
        try {
            JobReport jobReport = event.getJobReport();
            ScheduleRecordEntity scheduleRecord = commonRepository.findById(event.getId(), ScheduleRecordEntity.class)
                .orElseThrow(() -> ExceptionUtils.mpe("The ScheduleRecord not exist! id = %s", event.getId()));
            ScheduleEntity scheduleEntity = commonRepository
                .findById(scheduleRecord.getScheduleId(), ScheduleEntity.class)
                .orElseThrow(
                    () -> ExceptionUtils.mpe("The Schedule not exist! name = %s", scheduleRecord.getScheduleName()));
            NoticeType noticeType = Objects.requireNonNullElse(scheduleEntity.getNoticeType(), NoticeType.CLOSE);
            TestReportEmailModel testReportEmailModel = buildTestReportEmailModel(event);
            testReportEmailModel.setScheduleName(scheduleEntity.getName());
            testReportEmailModel.setProjectId(scheduleEntity.getProjectId());
            if (noticeType == NoticeType.ALL) {
                sendEmail(scheduleEntity.getEmails(), testReportEmailModel);
            }
            if (noticeType == NoticeType.SUCCESS && jobReport.getJobStatus() == JobStatus.SUCCESS) {
                sendEmail(scheduleEntity.getEmails(), testReportEmailModel);
            }
            if (noticeType == NoticeType.FAIL && jobReport.getJobStatus() == JobStatus.FAIL) {
                sendEmail(scheduleEntity.getEmails(), testReportEmailModel);
            }
        } catch (ApiTestPlatformException e) {
            log.error("Schedule test report custom error", e);
        } catch (Exception e) {
            log.error("Schedule test report system error!", e);
        }
    }


}
