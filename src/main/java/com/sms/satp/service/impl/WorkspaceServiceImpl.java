package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_WORKSPACE_CANNOT_DELETE_ERROR;
import static com.sms.satp.utils.Assert.isFalse;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import com.sms.satp.entity.workspace.Workspace;
import com.sms.satp.mapper.WorkspaceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.WorkspaceRepository;
import com.sms.satp.service.ProjectService;
import com.sms.satp.service.WorkspaceService;
import com.sms.satp.utils.ExceptionUtils;
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
    public Boolean add(WorkspaceRequest workspaceRequest) {
        log.info("WorkspaceService-add()-params: [Workspace]={}", workspaceRequest.toString());
        try {
            Workspace workspace = workspaceMapper.toEntity(workspaceRequest);
            workspaceRepository.insert(workspace);
        } catch (Exception e) {
            log.error("Failed to add the Workspace!", e);
            throw new ApiTestPlatformException(ADD_WORKSPACE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(WorkspaceRequest workspaceRequest) {
        log.info("WorkspaceService-edit()-params: [Workspace]={}", workspaceRequest.toString());
        try {
            boolean exists = workspaceRepository.existsById(workspaceRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Workspace", workspaceRequest.getId());
            }
            Workspace workspace = workspaceMapper.toEntity(workspaceRequest);
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
    public Boolean delete(String id) {
        try {
            isFalse(projectService.existsByWorkspaceId(id), THE_WORKSPACE_CANNOT_DELETE_ERROR);
            return commonDeleteRepository.deleteById(id, Workspace.class);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to delete the Workspace!", e);
            throw new ApiTestPlatformException(DELETE_WORKSPACE_BY_ID_ERROR);
        }
    }

}