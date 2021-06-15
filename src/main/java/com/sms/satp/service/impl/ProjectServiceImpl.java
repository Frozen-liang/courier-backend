package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.PROJECT;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_PROJECT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_PROJECT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_PROJECT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_PROJECT_EXIST_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ProjectRequest;
import com.sms.satp.dto.response.ProjectResponse;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.mapper.ProjectMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ProjectService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CommonDeleteRepository commonDeleteRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository,
        CommonDeleteRepository commonDeleteRepository,
        ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.commonDeleteRepository = commonDeleteRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectResponse findById(String id) {
        return projectRepository.findById(id).map(projectMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_BY_ID_ERROR));
    }

    @Override
    public List<ProjectResponse> list() {
        try {
            return projectMapper.toDtoList(projectRepository.findAllByRemovedOrderByCreateDateTimeDesc(Boolean.FALSE));
        } catch (Exception e) {
            log.error("Failed to get the Project list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = PROJECT, projectId = "id",
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
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Project!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT, projectId = "id",
        template = "{{#projectRequest.name}}")
    public Boolean edit(ProjectRequest projectRequest) {
        log.info("ProjectService-edit()-params: [Project]={}", projectRequest.toString());
        try {
            boolean exists = projectRepository.existsById(projectRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "Project", projectRequest.getId());
            ProjectEntity project = projectMapper.toEntity(projectRequest);
            projectRepository.save(project);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Project!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = PROJECT, projectId = "id",
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonDeleteRepository.deleteByIds(ids, ProjectEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the Project!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_BY_ID_ERROR);
        }
    }

}