package com.sms.satp.service;

import com.sms.satp.dto.request.ScheduleListRequest;
import com.sms.satp.dto.request.ScheduleRequest;
import com.sms.satp.dto.response.ScheduleResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    ScheduleResponse findById(String id);

    List<ScheduleResponse> list(ScheduleListRequest request);

    Boolean add(ScheduleRequest request);

    Boolean edit(ScheduleRequest request);

    Boolean delete(String id);
}
