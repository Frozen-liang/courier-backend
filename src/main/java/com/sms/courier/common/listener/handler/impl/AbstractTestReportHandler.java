package com.sms.courier.common.listener.handler.impl;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.modal.TestReportEmailModel;
import com.sms.courier.chat.modal.TestReportEmailModel.TestReportEmailModelBuilder;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.listener.event.TestReportEvent;
import com.sms.courier.common.listener.handler.TestReportHandler;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobReport;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public abstract class AbstractTestReportHandler implements TestReportHandler {

    private final Sender sender;

    public AbstractTestReportHandler(Sender sender) {
        this.sender = sender;
    }

    protected void sendEmail(List<String> emails, TestReportEmailModel model) {
        log.info("Send test report email to {}", emails);
        if (CollectionUtils.isEmpty(emails)) {
            return;
        }
        Map<AdditionalParam, Object> additionalParam = new HashMap<>();
        additionalParam.put(AdditionalParam.EMAIL_TO, emails);
        NotificationPayload notificationPayload = NotificationPayload.builder()
            .contentVariable(model)
            .titleVariable(model).additionalParam(additionalParam).build();
        sender.sendTestReportNotification(notificationPayload);
    }

    protected TestReportEmailModel buildTestReportEmailModel(TestReportEvent event) {
        JobReport jobReport = event.getJobReport();
        TestReportEmailModelBuilder testReportEmailModelBuilder = TestReportEmailModel.builder()
            .delayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost())
            .paramsTotalTimeCost(jobReport.getParamsTotalTimeCost())
            .totalTimeCost(jobReport.getTotalTimeCost())
            .caseName(event.getName())
            .dataName(event.getDataName());
        if (jobReport instanceof SceneCaseJobReport) {
            SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
            List<CaseReport> caseReportList = Objects.requireNonNullElse(sceneCaseJobReport.getCaseReportList(),
                Collections.emptyList());
            long success = caseReportList.stream().filter(report -> ResultType.SUCCESS == report.getIsSuccess())
                .count();
            testReportEmailModelBuilder.success(success);
            testReportEmailModelBuilder.fail(event.getCount() - success);
        } else {
            testReportEmailModelBuilder.success(jobReport.getJobStatus() == JobStatus.SUCCESS ? 1L : 0L);
            testReportEmailModelBuilder.fail(jobReport.getJobStatus() == JobStatus.FAIL ? 1L : 0L);
        }
        return testReportEmailModelBuilder.build();
    }

}
