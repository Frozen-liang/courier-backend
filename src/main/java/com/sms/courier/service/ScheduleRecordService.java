package com.sms.courier.service;

import com.sms.courier.dto.request.ScheduleRecordPageRequest;
import com.sms.courier.dto.response.ScheduleRecordResponse;
import org.springframework.data.domain.Page;

public interface ScheduleRecordService {

    Page<ScheduleRecordResponse> page(ScheduleRecordPageRequest request);
}
