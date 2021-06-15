package com.sms.satp.service;

import com.sms.satp.dto.request.AddSceneCaseGroupRequest;
import com.sms.satp.dto.request.SearchSceneCaseGroupRequest;
import com.sms.satp.dto.request.UpdateSceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import java.util.List;

public interface SceneCaseGroupService {

    Boolean add(AddSceneCaseGroupRequest request);

    Boolean edit(UpdateSceneCaseGroupRequest request);

    Boolean deleteById(String id);

    List<SceneCaseGroupResponse> getList(SearchSceneCaseGroupRequest request);
}
