package com.sms.satp.service;

import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.ProjectFunctionResponse;
import java.util.List;

public interface ProjectFunctionService {

    ProjectFunctionResponse findById(String id);

    List<Object> list(String projectId, String functionDesc, String functionName);

    Boolean add(ProjectFunctionRequest projectFunctionRequest);

    Boolean edit(ProjectFunctionRequest projectFunctionRequest);

    Boolean delete(String[] ids);
}