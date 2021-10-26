package com.sms.courier.engine.docker.service;

import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineSettingResponse;

public interface DockerService {

    void startContainer(EngineSettingResponse engineSetting);

    void queryLog(DockerLogRequest request);

    void deleteContainer(String name);

    void restartContainer(String name);
}
