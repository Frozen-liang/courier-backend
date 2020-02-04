package com.sms.satp.engine.service.impl;

import com.sms.satp.engine.TestEngine;
import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.service.ApiExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ApiExecuteServiceImpl implements ApiExecuteService {


    private final TestEngine testEngine;

    public ApiExecuteServiceImpl(TestEngine testEngine) {
        this.testEngine = testEngine;
    }

    @Override
    public ApiUnitResponse execute(ApiUnitRequest apiUnitRequest) {

        return testEngine.execute(apiUnitRequest);
    }


}
