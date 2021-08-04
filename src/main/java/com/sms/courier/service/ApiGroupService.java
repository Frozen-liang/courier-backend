package com.sms.courier.service;

import com.sms.courier.dto.request.ApiGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import java.util.List;

public interface ApiGroupService {

    List<TreeResponse> list(String projectId);

    Boolean add(ApiGroupRequest request);

    Boolean edit(ApiGroupRequest request);

    Boolean delete(String id);
}
