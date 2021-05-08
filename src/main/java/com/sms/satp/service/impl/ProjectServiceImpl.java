package com.sms.satp.service.impl;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.ProjectDto;
import com.sms.satp.service.ProjectService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<ProjectDto> list() {
        return null;
    }

    @Override
    public Page<ProjectDto> page(PageDto pageDto) {
        return null;
    }

    @Override
    public void add(ProjectDto projectDto) {

    }

    @Override
    public void edit(ProjectDto projectDto) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public ProjectDto findById(String id) {
        return null;
    }
}
