package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.service.GlobalEnvironmentService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.utils.PageDtoConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
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
    private final GlobalEnvironmentService globalEnvironmentService;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;
    private static final String CREATE_DATE_TIME = "createDateTime";
    private static final String PROJECT_ID = "projectId";

    public ProjectEnvironmentServiceImpl(ProjectEnvironmentRepository
        projectEnvironmentRepository, GlobalEnvironmentService globalEnvironmentService,
        ProjectEnvironmentMapper projectEnvironmentMapper) {
        this.projectEnvironmentRepository = projectEnvironmentRepository;
        this.globalEnvironmentService = globalEnvironmentService;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
    }

    @Override
    public Page<ProjectEnvironmentDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
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
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_PAGE_ERROR);
        }
    }

    @Override
    public List<Object> list(String projectId) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().projectId(projectId).build();
            List<Object> result = new ArrayList<>();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<ProjectEnvironment> example = Example.of(projectEnvironment, exampleMatcher);
            List<GlobalEnvironmentDto> globalEnvironments = globalEnvironmentService.list();
            List<ProjectEnvironment> projectEnvironments = projectEnvironmentRepository.findAll(example, sort);
            result.addAll(globalEnvironments);
            result
                .addAll(projectEnvironments.stream().map(projectEnvironmentMapper::toDto).collect(Collectors.toList()));
            return result;
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    public void add(ProjectEnvironmentDto projectEnvironmentDto) {
        log.info("ProjectEnvironmentService-add()-params: [ProjectEnvironment]={}", projectEnvironmentDto.toString());
        try {
            ProjectEnvironment projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentDto);
            projectEnvironmentRepository.insert(projectEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the projectEnvironment!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ENVIRONMENT_ERROR);
        }
    }

    @Override
    public void edit(ProjectEnvironmentDto projectEnvironmentDto) {
        log.info("ProjectEnvironmentService-edit()-params: [ProjectEnvironment]={}", projectEnvironmentDto.toString());
        try {
            ProjectEnvironment projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentDto);
            Optional<ProjectEnvironment> projectEnvironmentOptional = projectEnvironmentRepository
                .findById(projectEnvironment.getId());
            projectEnvironmentOptional.ifPresent(oldProjectEnvironment -> {
                projectEnvironment.setCreateDateTime(oldProjectEnvironment.getCreateDateTime());
                projectEnvironment.setCreateUserId(oldProjectEnvironment.getCreateUserId());
                projectEnvironmentRepository.save(projectEnvironment);
            });
        } catch (Exception e) {
            log.error("Failed to edit the projectEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ENVIRONMENT_ERROR);
        }
    }

    @Override
    public void delete(String[] ids) {
        try {
            Iterable<ProjectEnvironment> projectEnvironments = projectEnvironmentRepository
                .findAllById(Arrays.asList(ids));
            projectEnvironments.forEach(projectEnvironment -> projectEnvironment.setRemove(Boolean.TRUE));
            projectEnvironmentRepository.saveAll(projectEnvironments);
        } catch (Exception e) {
            log.error("Failed to delete the projectEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
    }

    @Override
    public ProjectEnvironmentDto findById(String id) {
        try {
            Optional<ProjectEnvironment> projectEnvironmentOptional
                = projectEnvironmentRepository.findById(id);
            return projectEnvironmentMapper.toDto(projectEnvironmentOptional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment by id!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
    }

}