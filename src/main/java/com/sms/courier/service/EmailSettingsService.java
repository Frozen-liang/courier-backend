package com.sms.courier.service;

import com.sms.courier.dto.request.EmailSettingsRequest;
import com.sms.courier.dto.response.EmailSettingsResponse;
import java.util.List;

public interface EmailSettingsService {

    EmailSettingsResponse findById(String id);

    List<EmailSettingsResponse> list();

    Boolean add(EmailSettingsRequest emailSettingsRequest);

    Boolean edit(EmailSettingsRequest emailSettingsRequest);

    Boolean delete(List<String> ids);
}
