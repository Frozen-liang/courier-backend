package com.sms.courier.service;

import com.sms.courier.dto.request.ScheduleJobRequest;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.dto.response.ScheduleSceneCaseJobResponse;
import java.util.List;

public interface ScheduleJobService {

    List<ScheduleCaseJobResponse> getCaseJobInfoString(ScheduleJobRequest request);

    List<ScheduleSceneCaseJobResponse> getSceneCaseJobInfo(ScheduleJobRequest request);
}
