package com.sms.courier.service;

import com.sms.courier.dto.request.ProjectImportFlowPageRequest;
import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProjectImportSourceService {

    Boolean create(ProjectImportSourceRequest projectImportSourceRequest);

    Boolean update(ProjectImportSourceRequest projectImportSourceRequest);

    ProjectImportSourceResponse findById(String id);

    ProjectImportSourceResponse findByProjectId(String projectId);

    Boolean delete(List<String> ids);

    Iterable<ProjectImportSourceEntity> findByIds(List<String> proImpSourceIds);

    ProjectImportFlowResponse getProjectImportFlow(String projectId);

    Page<ProjectImportFlowResponse> pageProjectImportFlow(ProjectImportFlowPageRequest request);
}
