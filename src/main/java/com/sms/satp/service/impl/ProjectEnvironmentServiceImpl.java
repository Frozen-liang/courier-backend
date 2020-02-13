package com.sms.satp.service.impl;

import com.sms.satp.entity.ProjectEnvironment;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.service.ProjectEnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectEnvironmentServiceImpl implements ProjectEnvironmentService {

    private final ProjectEnvironmentRepository projectEnvironmentRepository;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;

    public ProjectEnvironmentServiceImpl(ProjectEnvironmentRepository
            projectEnvironmentRepository, ProjectEnvironmentMapper projectEnvironmentMapper) {
        this.projectEnvironmentRepository = projectEnvironmentRepository;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
    }

    @Override
    public Page<ProjectEnvironmentDto> page(PageDto pageDto, String projectId) {
        try {
            ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
                .projectId(projectId)
                .build();
            Example<ProjectEnvironment> example = Example.of(projectEnvironment);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return projectEnvironmentRepository.findAll(example, pageable)
                .map(projectEnvironmentMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment page!", e);
            throw e;
        }
    }

    @Override
    public void add(ProjectEnvironmentDto projectEnvironmentDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ProjectEnvironmentService-add()-Parameter: %s",
                projectEnvironmentDto.toString()));
        }
        try {
            projectEnvironmentRepository.insert(
                projectEnvironmentMapper.toEntity(projectEnvironmentDto));
        } catch (Exception e) {
            log.error("Failed to add the projectEnvironment!", e);
            throw e;
        }
    }

    @Override
    public void edit(ProjectEnvironmentDto projectEnvironmentDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ProjectEnvironmentService-edit()-Parameter: %s",
                projectEnvironmentDto.toString()));
        }
        try {
            projectEnvironmentRepository.save(
                projectEnvironmentMapper.toEntity(projectEnvironmentDto));
        } catch (Exception e) {
            log.error("Failed to edit the projectEnvironment!", e);
            throw e;
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            projectEnvironmentRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the projectEnvironment!", e);
            throw e;
        }
    }
}