package com.sms.courier.engine;

import com.sms.courier.dto.request.EngineSettingRequest;
import com.sms.courier.dto.response.EngineSettingResponse;

public interface EngineSettingService {

    EngineSettingResponse findOne();

    Boolean edit(EngineSettingRequest request);
}
