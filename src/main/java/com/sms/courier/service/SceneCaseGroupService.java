package com.sms.courier.service;

import com.sms.courier.dto.request.SceneCaseGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import java.util.List;

public interface SceneCaseGroupService {

    Boolean add(SceneCaseGroupRequest request);

    Boolean edit(SceneCaseGroupRequest request);

    Boolean deleteById(String id);

    List<TreeResponse> list(String projectId);
}
