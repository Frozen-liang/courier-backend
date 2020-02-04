package com.sms.satp.engine;

import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;

public interface TestEngine {

    ApiUnitResponse execute(ApiUnitRequest request);


}
