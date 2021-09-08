package com.sms.courier.service;

import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;

public interface EmailService {

    EmailServiceEntity getEmailServiceEntity();

    EmailPropertiesResponse getEmailConfigurationResponse();

    EmailServiceEntity getEntityWithDecryptPwd();

    boolean isServiceEnabled();
}
