package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.ProjectEnvironmentRequest;
import com.sms.satp.dto.ProjectEnvironmentResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProjectEnvironmentService {

    Page<ProjectEnvironmentResponse> page(PageDto pageDto, String projectId);

    void add(ProjectEnvironmentRequest projectEnvironmentRequest);

    void edit(ProjectEnvironmentRequest projectEnvironmentRequest);

    ProjectEnvironmentResponse findById(String id);

    List<Object> list(String projectId);

    void delete(String[] ids);
}
