package com.sms.courier.service;

import com.sms.courier.dto.request.UserEnvConnRequest;
import com.sms.courier.dto.response.UserEnvConnResponse;

public interface UserEnvConnService {

    UserEnvConnResponse userEnv(String projectId);

    UserEnvConnResponse userEnvConn(UserEnvConnRequest request);
}
