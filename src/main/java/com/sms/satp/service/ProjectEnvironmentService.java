package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import org.springframework.data.domain.Page;

public interface ProjectEnvironmentService {

    Page<ProjectEnvironmentDto> page(PageDto pageDto, String projectId);

    void add(ProjectEnvironmentDto projectEnvironmentDto);

    void edit(ProjectEnvironmentDto projectEnvironmentDto);

    void deleteById(String id);

    ProjectEnvironmentDto findById(String id);
}
