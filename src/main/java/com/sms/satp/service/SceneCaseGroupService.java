package com.sms.satp.service;

import com.sms.satp.dto.request.SceneCaseGroupRequest;
import com.sms.satp.dto.response.TreeResponse;
import java.util.List;

public interface SceneCaseGroupService {

    Boolean add(SceneCaseGroupRequest request);

    Boolean edit(SceneCaseGroupRequest request);

    Boolean deleteById(String id);

    List<TreeResponse> list(String projectId);
}
