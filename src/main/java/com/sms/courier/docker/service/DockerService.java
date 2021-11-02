package com.sms.courier.docker.service;

import com.sms.courier.docker.entity.ContainerSetting;
import com.sms.courier.dto.request.DockerLogRequest;

public interface DockerService {

    void startContainer(ContainerSetting containerSetting);

    void queryLog(DockerLogRequest request);

    void deleteContainer(String name);

    void restartContainer(String name);
}
