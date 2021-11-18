package com.sms.courier.service;

import com.sms.courier.dto.request.CourierSchedulerRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.CourierSchedulerResponse;

public interface CourierSchedulerService {

    Boolean edit(CourierSchedulerRequest request);

    CourierSchedulerResponse findOne();

    Boolean createCourierScheduler();

    Boolean restartCourierScheduler();

    Boolean deleteCourierScheduler();

    Boolean queryLog(DockerLogRequest request);
}
