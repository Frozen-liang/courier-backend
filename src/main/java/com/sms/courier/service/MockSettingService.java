package com.sms.courier.service;

import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;

public interface MockSettingService {

    Boolean editUrl(MockSettingRequest mockSettingRequest);

    MockSettingResponse get();

}
