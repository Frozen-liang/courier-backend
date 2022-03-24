package com.sms.courier.service;

import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ProjectEnvironmentRequest;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProjectEnvironmentService {

    Page<ProjectEnvironmentResponse> page(PageDto pageDto, String projectId);

    Boolean add(ProjectEnvironmentRequest projectEnvironmentRequest);

    Boolean edit(ProjectEnvironmentRequest projectEnvironmentRequest);

    ProjectEnvironmentResponse findById(String id);

    ProjectEnvironmentEntity findOne(String id);

    List<Object> list(String projectId, String workspaceId);

    Boolean delete(List<String> ids);

    List<ProjectEnvironmentResponse> findAllByProjectId(String projectId);

    List<ProjectEnvironmentEntity> findAll(List<String> id);

    ProjectEnvironmentResponse getOneById(String id);
}
