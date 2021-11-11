package com.sms.courier.docker.service;

import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;

public interface DockerService {

    void startContainer(ContainerInfo containerInfo);

    void queryLog(DockerLogRequest request);

    void deleteContainer(String name);

    void restartContainer(String name);

    ContainerSettingResponse findOne();

    Boolean editContainerSetting(ContainerSettingRequest request);
}
