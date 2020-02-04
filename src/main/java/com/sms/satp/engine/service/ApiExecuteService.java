package com.sms.satp.engine.service;

import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;


public interface ApiExecuteService {


    public ApiUnitResponse execute(ApiUnitRequest apiUnitRequest);
}
