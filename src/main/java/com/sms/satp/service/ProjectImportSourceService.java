package com.sms.satp.service;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import java.util.List;

public interface ProjectImportSourceService {

    Boolean create(ProjectImportSourceRequest projectImportSourceRequest);

    Boolean update(ProjectImportSourceRequest projectImportSourceRequest);

    ProjectImportSourceResponse findById(String id);

    List<ProjectImportSourceResponse> findByProjectId(String projectId);
}