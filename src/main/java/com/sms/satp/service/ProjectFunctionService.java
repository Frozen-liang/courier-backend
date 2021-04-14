package com.sms.satp.service;

import com.sms.satp.entity.dto.ProjectFunctionDto;
import java.util.List;

public interface ProjectFunctionService {

    ProjectFunctionDto findById(String id);

    List<Object> list(String projectId, String functionDesc, String functionName);

    void add(ProjectFunctionDto projectFunctionDto);

    void edit(ProjectFunctionDto projectFunctionDto);

    void delete(String id);
}