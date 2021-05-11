package com.sms.satp.service;

import com.sms.satp.dto.ProjectFunctionRequest;
import com.sms.satp.dto.ProjectFunctionResponse;
import java.util.List;

public interface ProjectFunctionService {

    ProjectFunctionResponse findById(String id);

    List<Object> list(String projectId, String functionDesc, String functionName);

    void add(ProjectFunctionRequest projectFunctionRequest);

    void edit(ProjectFunctionRequest projectFunctionRequest);

    void delete(String[] ids);
}