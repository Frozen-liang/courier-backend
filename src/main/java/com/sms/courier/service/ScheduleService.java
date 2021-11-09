package com.sms.courier.service;

import com.sms.courier.dto.request.ScheduleListRequest;
import com.sms.courier.dto.request.ScheduleRequest;
import com.sms.courier.dto.response.ScheduleResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    ScheduleResponse findById(String id);

    List<ScheduleResponse> list(ScheduleListRequest request);

    Boolean add(ScheduleRequest request);

    Boolean edit(ScheduleRequest request);

    Boolean delete(List<String> ids);

    void deleteByGroupId(String groupId);

    Boolean handle(String id);

    Boolean open(String id, boolean enable);

    Boolean removeCaseIds(List<String> caseIds);
}
