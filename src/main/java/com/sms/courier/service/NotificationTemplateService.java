package com.sms.courier.service;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.entity.notification.NotificationTemplateEntity;

public interface NotificationTemplateService {

    NotificationTemplateEntity findTemplateByType(NotificationTemplateType type);

    boolean save(NotificationTemplateRequest request);
}
