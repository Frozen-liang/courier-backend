package com.sms.courier.service;

import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import java.util.List;

public interface ScheduleJobService {

    List<ScheduleCaseJobResponse> getScheduleJobInfo(String scheduleRecordId);
}
