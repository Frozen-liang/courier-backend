package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import java.util.List;

public interface ApiTagGroupService {

    ApiTagGroupResponse findById(String id);

    List<ApiTagGroupResponse> list(String projectId);

    Boolean add(ApiTagGroupRequest apiTagGroupRequest);

    Boolean edit(ApiTagGroupRequest apiTagGroupRequest);

    Boolean delete(List<String> ids);
}