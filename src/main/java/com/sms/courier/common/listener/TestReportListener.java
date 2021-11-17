package com.sms.courier.common.listener;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.modal.TestReportEmailModel;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.common.listener.event.TestReportEvent;
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
public class TestReportListener {

    private final Sender sender;

    public TestReportListener(Sender sender) {
        this.sender = sender;
    }

    @EventListener
    public void doProcess(TestReportEvent event) {
        NoticeType noticeType = Objects.requireNonNullElse(event.getNoticeType(), NoticeType.CLOSE);
        if (noticeType == NoticeType.FAIL && event.getFail() > 0L) {
            sendEmail(event.getType(), event.getEmails(), buildTestReportEmailModel(event));
        }
        if (noticeType == NoticeType.SUCCESS && event.getFail() == 0L) {
            sendEmail(event.getType(), event.getEmails(), buildTestReportEmailModel(event));
        }
        if (noticeType == NoticeType.ALL) {
            sendEmail(event.getType(), event.getEmails(), buildTestReportEmailModel(event));
        }
    }


    private TestReportEmailModel buildTestReportEmailModel(TestReportEvent event) {
        return TestReportEmailModel.builder()
            .delayTimeTotalTimeCost(event.getTotalTimeCost())
            .paramsTotalTimeCost(event.getParamsTotalTimeCost())
            .totalTimeCost(event.getTotalTimeCost())
            .name(event.getName())
            .dataName(event.getDataName())
            .success(event.getSuccess())
            .fail(event.getFail())
            .testStartTime(event.getTestStartTime())
            .testCompletionTime(event.getTestCompletionTime())
            .build();
    }

    private void sendEmail(NotificationTemplateType type, List<String> emails, TestReportEmailModel model) {
        log.info("Send test report email to {}", emails);
        if (CollectionUtils.isEmpty(emails)) {
            return;
        }
        Map<AdditionalParam, Object> additionalParam = new HashMap<>();
        additionalParam.put(AdditionalParam.EMAIL_TO, emails);
        NotificationPayload notificationPayload = NotificationPayload.builder()
            .contentVariable(model)
            .titleVariable(model).additionalParam(additionalParam).build();
        sender.sendTestReportNotification(type, notificationPayload);
    }
}
