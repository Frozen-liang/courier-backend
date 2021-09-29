package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.WORKSPACE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_WORKSPACE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_WORKSPACE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_WORKSPACE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_COUNT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_WORKSPACE_CANNOT_DELETE_ERROR;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.WorkspaceField.USER_IDS;
import static com.sms.courier.utils.Assert.isFalse;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceResponse;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.mapper.WorkspaceMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.WorkspaceRepository;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.ProjectService;
import com.sms.courier.service.WorkspaceService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final ProjectService projectService;
    private final WorkspaceRepository workspaceRepository;
    private final CommonRepository commonRepository;
    private final WorkspaceMapper workspaceMapper;
    private final ApiTestCaseService apiTestCaseService;

    public WorkspaceServiceImpl(ProjectService projectService,
        WorkspaceRepository workspaceRepository,
        CommonRepository commonRepository,
        WorkspaceMapper workspaceMapper, ApiTestCaseService apiTestCaseService) {
        this.projectService = projectService;
        this.workspaceRepository = workspaceRepository;
        this.commonRepository = commonRepository;
        this.workspaceMapper = workspaceMapper;
        this.apiTestCaseService = apiTestCaseService;
    }

    @Override
    public WorkspaceResponse findById(String id) {
        return workspaceRepository.findById(id).map(workspaceMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_WORKSPACE_BY_ID_ERROR));
    }

    @Override
    public List<WorkspaceResponse> list() {
        try {
            String collectionName = CollectionName.WORKSPACE.getName();
            return commonRepository
                .listLookupUser(collectionName, List.of(REMOVE.is(Boolean.FALSE)), WorkspaceResponse.class);
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
            workspaceRepository.insert(workspace);
        } catch (Exception e) {
            log.error("Failed to add the Workspace!", e);
            throw new ApiTestPlatformException(ADD_WORKSPACE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = WORKSPACE, template = "{{#workspaceRequest.name}}", refId = "id")
    public Boolean edit(WorkspaceRequest workspaceRequest) {
        log.info("WorkspaceService-edit()-params: [Workspace]={}", workspaceRequest.toString());
        try {
            boolean exists = workspaceRepository.existsById(workspaceRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Workspace", workspaceRequest.getId());
            }
            WorkspaceEntity workspace = workspaceMapper.toEntity(workspaceRequest);
            workspaceRepository.save(workspace);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the Workspace!", e);
            throw new ApiTestPlatformException(EDIT_WORKSPACE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = WORKSPACE, template = "{{#result.name}}",
        enhance = @Enhance(enable = true), refId = "id")
    public Boolean delete(String id) {
        try {
            isFalse(projectService.existsByWorkspaceId(id), THE_WORKSPACE_CANNOT_DELETE_ERROR);
            return commonRepository.deleteById(id, WorkspaceEntity.class);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to delete the Workspace!", e);
            throw new ApiTestPlatformException(DELETE_WORKSPACE_BY_ID_ERROR);
        }
    }

    @Override
    public List<WorkspaceResponse> findByUserId() {
        return commonRepository.listLookupUser(CollectionName.WORKSPACE.getName(),
            List.of(REMOVE.is(Boolean.FALSE), USER_IDS.is(new ObjectId(SecurityUtil.getCurrUserId()))),
            WorkspaceResponse.class
        );
    }

    @Override
    public Long caseCount(String id) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(id);
            if (CollectionUtils.isNotEmpty(projectResponses)) {
                List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                    .collect(Collectors.toList());
                return apiTestCaseService.countByProjectIds(projectIds);
            }
            return 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case count!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_COUNT_ERROR);
        }
    }

}