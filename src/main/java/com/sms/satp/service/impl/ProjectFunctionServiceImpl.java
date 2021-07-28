package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.PROJECT_FUNCTION;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_FUNCTION_LIST_ERROR;
import static com.sms.satp.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonField.PROJECT_ID;
import static com.sms.satp.common.field.CommonField.REMOVE;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.FunctionResponse;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.dto.response.ProjectFunctionResponse;
import com.sms.satp.entity.function.FunctionMessage;
import com.sms.satp.entity.function.ProjectFunctionEntity;
import com.sms.satp.mapper.ProjectFunctionMapper;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.ProjectFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import com.sms.satp.service.MessageService;
import com.sms.satp.service.ProjectFunctionService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectFunctionServiceImpl implements ProjectFunctionService {

    private final ProjectFunctionRepository projectFunctionRepository;
    private final ProjectFunctionMapper projectFunctionMapper;
    private final GlobalFunctionService globalFunctionService;
    private final MessageService messageService;
    private final CommonRepository commonRepository;
    private static final String FUNCTION_KEY = "functionKey";

    public ProjectFunctionServiceImpl(ProjectFunctionRepository projectFunctionRepository,
        ProjectFunctionMapper projectFunctionMapper, GlobalFunctionService globalFunctionService,
        MessageService messageService, CommonRepository commonRepository) {
        this.projectFunctionRepository = projectFunctionRepository;
        this.projectFunctionMapper = projectFunctionMapper;
        this.globalFunctionService = globalFunctionService;
        this.messageService = messageService;
        this.commonRepository = commonRepository;
    }

    @Override
    public ProjectFunctionResponse findById(String id) {
        return projectFunctionRepository.findById(id).map(projectFunctionMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_FUNCTION_BY_ID_ERROR));
    }

    @Override
    public List<FunctionResponse> list(String projectId, String workspaceId, String functionKey, String functionName) {
        try {
            ProjectFunctionEntity projectFunction = ProjectFunctionEntity.builder()
                .projectId(projectId).functionKey(functionKey).functionName(functionName).build();
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(FUNCTION_KEY, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<ProjectFunctionEntity> example = Example.of(projectFunction, matcher);
            List<ProjectFunctionEntity> projectFunctions = projectFunctionRepository.findAll(example, sort);
            ArrayList<FunctionResponse> list = new ArrayList<>();
            List<GlobalFunctionResponse> globalFunctionList = globalFunctionService.list(workspaceId, functionKey,
                functionName);
            list.addAll(globalFunctionList);
            list.addAll(projectFunctionMapper.toDtoList(projectFunctions));
            return list;
        } catch (Exception e) {
            log.error("Failed to get the ProjectFunction list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_FUNCTION_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = PROJECT_FUNCTION,
        template = "{{#projectFunctionRequest.functionName}}")
    public Boolean add(ProjectFunctionRequest projectFunctionRequest) {
        log.info("ProjectFunctionService-add()-params: [ProjectFunction]={}", projectFunctionRequest.toString());
        try {
            ProjectFunctionEntity projectFunction = projectFunctionMapper.toEntity(projectFunctionRequest);
            projectFunctionRepository.insert(projectFunction);
            sendMessageToEngine(List.of(projectFunction.getId()), ADD, projectFunction.getProjectId());
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT_FUNCTION,
        template = "{{#projectFunctionRequest.functionName}}")
    public Boolean edit(ProjectFunctionRequest projectFunctionRequest) {
        log.info("ProjectFunctionService-edit()-params: [ProjectFunction]={}", projectFunctionRequest.toString());
        try {
            boolean exists = projectFunctionRepository.existsById(projectFunctionRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ProjectFunction", projectFunctionRequest.getId());
            ProjectFunctionEntity projectFunction = projectFunctionMapper.toEntity(projectFunctionRequest);
            projectFunctionRepository.save(projectFunction);
            sendMessageToEngine(List.of(projectFunction.getId()), EDIT, projectFunction.getProjectId());
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = PROJECT_FUNCTION,
        template = "{{#result?.![#this.functionName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            String projectId = projectFunctionRepository.findById(ids.get(0)).map(ProjectFunctionEntity::getProjectId)
                .orElse(null);
            Boolean result = commonRepository.deleteByIds(ids, ProjectFunctionEntity.class);
            sendMessageToEngine(ids, DELETE, projectId);
            return result;
        } catch (Exception e) {
            log.error("Failed to delete the ProjectFunction!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public Map<String, List<ProjectFunctionResponse>> findAll() {
        return projectFunctionRepository.findAllByRemovedIsFalse()
            .collect(Collectors.groupingBy(ProjectFunctionResponse::getProjectId));
    }

    @Override
    public List<ProjectFunctionResponse> pullFunction(List<String> ids) {
        return projectFunctionRepository.findAllByIdIn(ids);
    }

    private void sendMessageToEngine(List<String> ids, OperationType operationType, String projectId) {
        FunctionMessage functionMessage = FunctionMessage.builder()
            .ids(ids)
            .global(true)
            .key(projectId)
            .operationType(operationType.getCode())
            .build();
        messageService.enginePullFunctionMessage(functionMessage);
    }

}