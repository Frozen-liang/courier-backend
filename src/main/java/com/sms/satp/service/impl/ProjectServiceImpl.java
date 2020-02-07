package com.sms.satp.service.impl;

import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.mapper.ProjectMapper;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ProjectService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDto> list() {
        return projectRepository.findAll()
            .stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<ProjectDto> page(PageDto pageDto) {
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        return projectRepository.findAll(pageable).map(projectMapper::toDto);
    }

    @Override
    public void add(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        project.setCreateDateTime(LocalDateTime.now());
        projectRepository.insert(project);
    }

    @Override
    public void edit(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        project.setModifyDateTime(LocalDateTime.now());
        projectRepository.save(project);
    }

    @Override
    public void delete(String id) {
        projectRepository.deleteById(id);
    }
}