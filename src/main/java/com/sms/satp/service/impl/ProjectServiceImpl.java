package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.mapper.ProjectMapper;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ProjectService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
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
        try {
            return projectRepository.findAll()
                .stream().map(projectMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the project list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_LIST_ERROR);
        }
    }

    @Override
    public Page<ProjectDto> page(PageDto pageDto) {
        try {
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return projectRepository.findAll(pageable).map(projectMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the project page!", e);
            throw new ApiTestPlatformException(GET_PROJECT_PAGE_ERROR);
        }
    }

    @Override
    public void add(ProjectDto projectDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ProjectService-add()-Parameter: %s",
                projectDto.toString()));
        }
        try {
            Project project = projectMapper.toEntity(projectDto);
            project.setCreateDateTime(LocalDateTime.now());
            projectRepository.insert(project);
        } catch (Exception e) {
            log.error("Failed to add the project!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ERROR);
        }
    }

    @Override
    public void edit(ProjectDto projectDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("ProjectService-edit()-Parameter: %s",
                projectDto.toString()));
        }
        try {
            Project project = projectMapper.toEntity(projectDto);
            project.setModifyDateTime(LocalDateTime.now());
            projectRepository.save(project);
        } catch (Exception e) {
            log.error("Failed to edit the project!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ERROR);
        }
    }

    @Override
    public void delete(String id) {
        try {
            projectRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the project!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_BY_ID_ERROR);
        }
    }
}