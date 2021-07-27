package com.sms.satp.service;

import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.FunctionResponse;
import com.sms.satp.dto.response.ProjectFunctionResponse;
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
}