package com.sms.satp.service;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportFlowResponse;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import java.util.List;

public interface ProjectImportSourceService {

    Boolean create(ProjectImportSourceRequest projectImportSourceRequest);

    Boolean update(ProjectImportSourceRequest projectImportSourceRequest);

    ProjectImportSourceResponse findById(String id);

    List<ProjectImportSourceResponse> findByProjectId(String projectId);

    Boolean delete(List<String> ids);

    Iterable<ProjectImportSourceEntity> findByIds(List<String> proImpSourceIds);

    ProjectImportFlowResponse getProjectImportFlow(String projectId);

}
