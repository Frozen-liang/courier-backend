package com.sms.courier.service;

import com.sms.courier.dto.request.ScheduleJobRequest;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.dto.response.ScheduleSceneCaseJobResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ScheduleJobService {

    List<ScheduleCaseJobResponse> getCaseJobInfoString(ScheduleJobRequest request);

    Page<ScheduleSceneCaseJobResponse> getSceneCaseJobInfo(ScheduleJobRequest request);
}
