package com.sms.courier.service;

import com.sms.courier.dto.request.EmailRequest;

public interface EmailSettingService {

    boolean updateEmailConfiguration(EmailRequest emailRequest);

    boolean enable();

    boolean disable();
}
