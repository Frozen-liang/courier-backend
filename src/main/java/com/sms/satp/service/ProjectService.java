package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProjectService {

    List<ProjectDto> list();

    Page<ProjectDto> page(PageDto pageDto);

    void add(ProjectDto projectDto);

    void edit(ProjectDto projectDto);

    void delete(String id);

    ProjectDto findById(String id);

}
