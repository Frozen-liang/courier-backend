package com.sms.satp.service;

import com.sms.satp.dto.request.ProjectRequest;
import com.sms.satp.dto.response.ProjectResponse;
import java.util.List;

public interface ProjectService {

    ProjectResponse findById(String id);

    List<ProjectResponse> list(String workspaceId);

    Boolean add(ProjectRequest projectRequest);

    Boolean edit(ProjectRequest projectRequest);

    Boolean delete(List<String> ids);

    boolean existsByWorkspaceId(String workspaceId);
}