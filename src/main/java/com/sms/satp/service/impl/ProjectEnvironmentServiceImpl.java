package com.sms.satp.service.impl;

import com.sms.satp.entity.ProjectEnvironment;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.service.ProjectEnvironmentService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

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
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .projectId(projectId)
            .build();
        Example<ProjectEnvironment> example = Example.of(projectEnvironment);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        return projectEnvironmentRepository.findAll(example, pageable)
            .map(projectEnvironmentMapper::toDto);
    }

    @Override
    public void add(ProjectEnvironmentDto projectEnvironmentDto) {
        projectEnvironmentRepository.insert(
            projectEnvironmentMapper.toEntity(projectEnvironmentDto));
    }

    @Override
    public void edit(ProjectEnvironmentDto projectEnvironmentDto) {
        projectEnvironmentRepository.save(
            projectEnvironmentMapper.toEntity(projectEnvironmentDto));
    }

    @Override
    public void deleteById(String id) {
        projectEnvironmentRepository.deleteById(id);
    }
}