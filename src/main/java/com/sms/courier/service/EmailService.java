package com.sms.courier.service;

import com.sms.courier.dto.response.EmailConfigurationResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;

public interface EmailService {

    EmailServiceEntity getEmailServiceEntity();

    EmailConfigurationResponse getEmailConfigurationResponse();

    EmailServiceEntity getEntityWithDecryptPwd();

    boolean isServiceEnabled();
}
