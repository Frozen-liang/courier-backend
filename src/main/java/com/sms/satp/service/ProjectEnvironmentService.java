package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProjectEnvironmentService {

    Page<ProjectEnvironmentResponse> page(PageDto pageDto, String projectId);

    Boolean add(ProjectEnvironmentRequest projectEnvironmentRequest);

    Boolean edit(ProjectEnvironmentRequest projectEnvironmentRequest);

    ProjectEnvironmentResponse findById(String id);

    List<Object> list(String projectId);

    Boolean delete(String[] ids);
}
