package com.sms.satp.service;

import com.sms.satp.entity.dto.ProjectFunctionDto;
import java.util.List;
import org.bson.types.ObjectId;

public interface ProjectFunctionService {

    ProjectFunctionDto findById(ObjectId id);

    List<Object> list(String projectId, String functionDesc, String functionName);

    void add(ProjectFunctionDto projectFunctionDto);

    void edit(ProjectFunctionDto projectFunctionDto);

    void delete(ObjectId id);
}