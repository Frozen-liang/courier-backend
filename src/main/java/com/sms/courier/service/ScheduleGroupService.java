package com.sms.courier.service;

import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import java.util.List;

public interface ScheduleGroupService {

    ScheduleGroupResponse findById(String id);

    List<ScheduleGroupResponse> list(String projectId);

    Boolean add(ScheduleGroupRequest scheduleGroupRequest);

    Boolean edit(ScheduleGroupRequest scheduleGroupRequest);

    Boolean delete(String id);
}
