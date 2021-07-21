package com.sms.satp.service;

import com.sms.satp.dto.request.ApiGroupRequest;
import com.sms.satp.dto.response.ApiGroupResponse;
import java.util.List;

public interface ApiGroupService {

    List<ApiGroupResponse> list(String projectId, String groupId);

    Boolean add(ApiGroupRequest request);

    Boolean edit(ApiGroupRequest request);

    Boolean delete(String id);
}
