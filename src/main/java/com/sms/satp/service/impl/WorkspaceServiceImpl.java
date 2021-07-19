package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.WORKSPACE;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_WORKSPACE_CANNOT_DELETE_ERROR;
import static com.sms.satp.utils.Assert.isFalse;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import com.sms.satp.entity.workspace.WorkspaceEntity;
import com.sms.satp.mapper.WorkspaceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.WorkspaceRepository;
import com.sms.satp.service.ProjectService;
import com.sms.satp.service.WorkspaceService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.SecurityUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final ProjectService projectService;
    private final WorkspaceRepository workspaceRepository;
    private final CommonDeleteRepository commonDeleteRepository;
    private final WorkspaceMapper workspaceMapper;

    public WorkspaceServiceImpl(ProjectService projectService,
        WorkspaceRepository workspaceRepository,
        CommonDeleteRepository commonDeleteRepository,
        WorkspaceMapper workspaceMapper) {
        this.projectService = projectService;
        this.workspaceRepository = workspaceRepository;
        this.commonDeleteRepository = commonDeleteRepository;
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public WorkspaceResponse findById(String id) {
        return workspaceRepository.findById(id).map(workspaceMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_WORKSPACE_BY_ID_ERROR));
    }

    @Override
    public List<WorkspaceResponse> list() {
        try {
            return workspaceMapper.toDtoList(workspaceRepository.findAllByRemovedIsFalseOrderByCreateDateTimeDesc());
        } catch (Exception e) {
            log.error("Failed to get the Workspace list!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = WORKSPACE, template = "{{#workspaceRequest.name}}")
    public Boolean add(WorkspaceRequest workspaceRequest) {
        log.info("WorkspaceService-add()-params: [Workspace]={}", workspaceRequest.toString());
        try {
            WorkspaceEntity workspace = workspaceMapper.toEntity(workspaceRequest);
            workspace.setCreateUsername(SecurityUtil.getCurrentUser().getUsername());
            workspaceRepository.insert(workspace);
        } catch (Exception e) {
            log.error("Failed to add the Workspace!", e);
            throw new ApiTestPlatformException(ADD_WORKSPACE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = WORKSPACE, template = "{{#workspaceRequest.name}}")
    public Boolean edit(WorkspaceRequest workspaceRequest) {
        log.info("WorkspaceService-edit()-params: [Workspace]={}", workspaceRequest.toString());
        try {
            boolean exists = workspaceRepository.existsById(workspaceRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Workspace", workspaceRequest.getId());
            }
            WorkspaceEntity workspace = workspaceMapper.toEntity(workspaceRequest);
            workspaceRepository.save(workspace);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Workspace!", e);
            throw new ApiTestPlatformException(EDIT_WORKSPACE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = WORKSPACE, template = "{{#result.name}}",
        enhance = @Enhance(enable = true))
    public Boolean delete(String id) {
        try {
            isFalse(projectService.existsByWorkspaceId(id), THE_WORKSPACE_CANNOT_DELETE_ERROR);
            return commonDeleteRepository.deleteById(id, WorkspaceEntity.class);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to delete the Workspace!", e);
            throw new ApiTestPlatformException(DELETE_WORKSPACE_BY_ID_ERROR);
        }
    }

    @Override
    public List<WorkspaceResponse> findByUserId(String userId) {
        return workspaceMapper
            .toDtoList(workspaceRepository.findAllByRemovedIsFalseAndUserIdsContainsOrderByCreateDateTimeDesc(userId));
    }

}