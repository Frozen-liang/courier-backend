package com.sms.satp.service;

import com.sms.satp.dto.request.ApiGroupRequest;
import com.sms.satp.dto.response.TreeResponse;
import java.util.List;

public interface ApiGroupService {

    List<TreeResponse> list(String projectId);

    Boolean add(ApiGroupRequest request);

    Boolean edit(ApiGroupRequest request);

    Boolean delete(String id);
}
