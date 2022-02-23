package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.PROJECT_ENV;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_PAGE_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ProjectEnvironmentRequest;
import com.sms.courier.dto.response.GlobalEnvironmentResponse;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.mapper.ProjectEnvironmentMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectEnvironmentRepository;
import com.sms.courier.service.GlobalEnvironmentService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.PageDtoConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
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
    private final CommonRepository commonRepository;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;

    public ProjectEnvironmentServiceImpl(ProjectEnvironmentRepository
        projectEnvironmentRepository, GlobalEnvironmentService globalEnvironmentService,
        CommonRepository commonRepository,
        ProjectEnvironmentMapper projectEnvironmentMapper) {
        this.projectEnvironmentRepository = projectEnvironmentRepository;
        this.globalEnvironmentService = globalEnvironmentService;
        this.commonRepository = commonRepository;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
    }

    @Override
    public Page<ProjectEnvironmentResponse> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder()
                .projectId(projectId)
                .build();
            Example<ProjectEnvironmentEntity> example = Example.of(projectEnvironment);
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
    public List<Object> list(String projectId, String workspaceId) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
            ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().projectId(projectId)
                .build();
            List<Object> result = new ArrayList<>();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getName(), GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getName(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<ProjectEnvironmentEntity> example = Example.of(projectEnvironment, exampleMatcher);
            List<GlobalEnvironmentResponse> globalEnvironments = globalEnvironmentService.list(workspaceId);
            List<ProjectEnvironmentEntity> projectEnvironments = projectEnvironmentRepository.findAll(example, sort);
            result.addAll(globalEnvironments);
            result.addAll(projectEnvironmentMapper.toDtoList(projectEnvironments));
            return result;
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = PROJECT_ENV,
        template = "{{#projectEnvironmentRequest.envName}}")
    public Boolean add(ProjectEnvironmentRequest projectEnvironmentRequest) {
        log.info("ProjectEnvironmentService-add()-params: [ProjectEnvironment]={}",
            projectEnvironmentRequest.toString());
        try {
            ProjectEnvironmentEntity projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentRequest);
            projectEnvironmentRepository.insert(projectEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the projectEnvironment!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT_ENV,
        template = "{{#projectEnvironmentRequest.envName}}",
        sourceId = "{{#projectEnvironmentRequest.id}}")
    public Boolean edit(ProjectEnvironmentRequest projectEnvironmentRequest) {
        log.info("ProjectEnvironmentService-edit()-params: [ProjectEnvironment]={}",
            projectEnvironmentRequest.toString());
        try {
            boolean exists = projectEnvironmentRepository.existsById(projectEnvironmentRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ProjectEnvironment", projectEnvironmentRequest.getId());
            ProjectEnvironmentEntity projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentRequest);
            projectEnvironmentRepository.save(projectEnvironment);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to edit the projectEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = PROJECT_ENV,
        template = "{{#result?.![#this.envName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, ProjectEnvironmentEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the projectEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
    }

    @Override
    public List<ProjectEnvironmentResponse> findAllByProjectId(String projectId) {
        return projectEnvironmentMapper
            .toDtoList(projectEnvironmentRepository.findAllByProjectIdAndRemoved(projectId, Boolean.FALSE));
    }

    @Override
    public List<ProjectEnvironmentEntity> findAll(List<String> id) {
        return id.stream().map(nevId -> projectEnvironmentRepository.findById(nevId)
            .orElse(projectEnvironmentMapper.toEntityByGlobal(globalEnvironmentService.findOne(nevId))))
            .collect(Collectors.toList());
    }

    @Override
    public ProjectEnvironmentResponse findById(String id) {
        return projectEnvironmentRepository.findById(id).map(projectEnvironmentMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR));
    }

    @Override
    public ProjectEnvironmentEntity findOne(String id) {
        return projectEnvironmentRepository.findById(id)
            .orElse(projectEnvironmentMapper.toEntityByGlobal(globalEnvironmentService.findOne(id)));
    }

}