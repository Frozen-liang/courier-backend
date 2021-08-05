package com.sms.courier.service;

import com.sms.courier.dto.request.ApiTagGroupRequest;
import com.sms.courier.dto.response.ApiTagGroupResponse;
import java.util.List;

public interface ApiTagGroupService {

    ApiTagGroupResponse findById(String id);

    List<ApiTagGroupResponse> list(String projectId);

    Boolean add(ApiTagGroupRequest apiTagGroupRequest);

    Boolean edit(ApiTagGroupRequest apiTagGroupRequest);

    Boolean delete(List<String> ids);
}