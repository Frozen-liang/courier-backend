package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.PROJECT;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_PROJECT_EXIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.mapper.ProjectMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.service.ProjectService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CommonRepository commonRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, CommonRepository commonRepository,
        ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.commonRepository = commonRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectResponse findById(String id) {
        return projectRepository.findById(id).map(projectMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_BY_ID_ERROR));
    }

    @Override
    public List<ProjectResponse> list(String workspaceId) {
        try {
            return projectMapper.toDtoList(
                projectRepository.findAllByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(workspaceId));
        } catch (Exception e) {
            log.error("Failed to get the Project list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = PROJECT, refId = "workspaceId",
        template = "{{#projectRequest.name}}")
    public Boolean add(ProjectRequest projectRequest) {
        log.info("ProjectService-add()-params: [Project]={}", projectRequest.toString());
        try {
            ProjectEntity project = projectMapper.toEntity(projectRequest);
            Example<ProjectEntity> example = Example.of(project);
            Optional<ProjectEntity> optional = projectRepository.findOne(example);
            if (optional.isEmpty()) {
                projectRepository.insert(project);
                return Boolean.TRUE;
            }
            throw ExceptionUtils.mpe(THE_PROJECT_EXIST_ERROR, project.getName(), project.getVersion());
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the Project!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT, refId = "id",
        template = "{{#projectRequest.name}}")
    public Boolean edit(ProjectRequest projectRequest) {
        log.info("ProjectService-edit()-params: [Project]={}", projectRequest.toString());
        try {
            boolean exists = projectRepository.existsById(projectRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "Project", projectRequest.getId());
            ProjectEntity project = projectMapper.toEntity(projectRequest);
            projectRepository.save(project);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the Project!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = PROJECT, refId = "id",
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, ProjectEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the Project!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_BY_ID_ERROR);
        }
    }

    @Override
    public boolean existsByWorkspaceId(String workspaceId) {
        return projectRepository.existsByWorkspaceIdAndRemovedIsFalse(workspaceId);
    }

}