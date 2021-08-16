package com.sms.courier.service;

import com.sms.courier.dto.request.ProjectFunctionRequest;
import com.sms.courier.dto.response.FunctionResponse;
import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.dto.response.ProjectFunctionResponse;
import java.util.List;
import java.util.Map;

public interface ProjectFunctionService {

    ProjectFunctionResponse findById(String id);

    List<FunctionResponse> list(String projectId, String workspaceId, String functionDesc, String functionName);

    Boolean add(ProjectFunctionRequest projectFunctionRequest);

    Boolean edit(ProjectFunctionRequest projectFunctionRequest);

    Boolean delete(List<String> ids);

    Map<String, List<ProjectFunctionResponse>> findAll();

    List<ProjectFunctionResponse> pullFunction(List<String> ids);

    List<LoadFunctionResponse> loadFunction(String workspaceId, String projectId);
}