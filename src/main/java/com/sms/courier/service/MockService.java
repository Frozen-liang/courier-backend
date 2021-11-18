package com.sms.courier.service;

import com.sms.courier.dto.request.DockerLogRequest;

public interface MockService {

    Boolean createMock();

    Boolean restartMock();

    Boolean deleteMock();

    Boolean queryLog(DockerLogRequest request);
}
