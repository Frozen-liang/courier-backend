package com.sms.courier.chat.sender;

import com.sms.courier.chat.modal.NotificationPayload;

public interface Sender {

    void updateConfiguration();

    boolean validateConnection();

    boolean sendResetPwdNotification(NotificationPayload notificationPayload);

    boolean sendTestReportNotification(NotificationPayload notificationPayload);

}
