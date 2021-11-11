package com.sms.courier.service;

import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.TestCaseCountStatisticsResponse;
import java.util.List;

public interface ProjectService {

    ProjectResponse findById(String id);

    List<ProjectResponse> list(String workspaceId);

    Boolean add(ProjectRequest projectRequest);

    Boolean edit(ProjectRequest projectRequest);

    Boolean delete(List<String> ids);

    boolean existsByWorkspaceId(String workspaceId);

    List<TestCaseCountStatisticsResponse> caseGroupDayCount(String projectId);
}