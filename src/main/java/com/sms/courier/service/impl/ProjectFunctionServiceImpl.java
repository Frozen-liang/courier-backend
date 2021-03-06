package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.PROJECT_FUNCTION;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_FUNCTION_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_FUNCTION_KEY_EXIST_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.utils.Assert.isFalse;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.FunctionField;
import com.sms.courier.dto.request.ProjectFunctionRequest;
import com.sms.courier.dto.response.FunctionResponse;
import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.dto.response.ProjectFunctionResponse;
import com.sms.courier.engine.listener.event.EngineProjectFunctionEvent;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.mapper.ProjectFunctionMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedFunctionRepository;
import com.sms.courier.repository.ProjectFunctionRepository;
import com.sms.courier.service.GlobalFunctionService;
import com.sms.courier.service.ProjectFunctionService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final CommonRepository commonRepository;
    private final CustomizedFunctionRepository customizedFunctionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ProjectFunctionServiceImpl(ProjectFunctionRepository projectFunctionRepository,
        ProjectFunctionMapper projectFunctionMapper, GlobalFunctionService globalFunctionService,
        CommonRepository commonRepository,
        CustomizedFunctionRepository customizedFunctionRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.projectFunctionRepository = projectFunctionRepository;
        this.projectFunctionMapper = projectFunctionMapper;
        this.globalFunctionService = globalFunctionService;
        this.commonRepository = commonRepository;
        this.customizedFunctionRepository = customizedFunctionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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
                .withMatcher(FunctionField.FUNCTION_KEY.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
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
            String functionKey = projectFunction.getFunctionKey();
            boolean exists = projectFunctionRepository
                .existsByFunctionKeyAndProjectIdAndRemovedIsFalse(functionKey, projectFunction.getProjectId());
            isFalse(exists, THE_FUNCTION_KEY_EXIST_ERROR, functionKey, "ProjectFunction");
            projectFunctionRepository.insert(projectFunction);
            publishEvent(ADD, Collections.singletonList(projectFunction));
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = PROJECT_FUNCTION,
        template = "{{#projectFunctionRequest.functionName}}",
        sourceId = "{{#projectFunctionRequest.id}}")
    public Boolean edit(ProjectFunctionRequest projectFunctionRequest) {
        log.info("ProjectFunctionService-edit()-params: [ProjectFunction]={}", projectFunctionRequest.toString());
        try {
            ProjectFunctionEntity oldProjectFunction = projectFunctionRepository
                .findById(projectFunctionRequest.getId())
                .orElseThrow(
                    () -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ProjectFunction", projectFunctionRequest.getId()));
            ProjectFunctionEntity projectFunction = projectFunctionMapper.toEntity(projectFunctionRequest);
            String functionKey = projectFunction.getFunctionKey();
            isFalse(!oldProjectFunction.getFunctionKey().equals(functionKey)
                    && projectFunctionRepository
                    .existsByFunctionKeyAndProjectIdAndRemovedIsFalse(functionKey, projectFunction.getProjectId()),
                THE_FUNCTION_KEY_EXIST_ERROR, functionKey, "ProjectFunction");
            projectFunctionRepository.save(projectFunction);
            publishEvent(EDIT, Collections.singletonList(projectFunction));
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = PROJECT_FUNCTION,
        template = "{{#result?.![#this.functionName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            Boolean result = commonRepository.deleteByIds(ids, ProjectFunctionEntity.class);
            publishEvent(DELETE, projectFunctionRepository.findByIdIn(ids));
            return result;
        } catch (Exception e) {
            log.error("Failed to delete the ProjectFunction!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }


    @Override
    public List<ProjectFunctionResponse> pullFunction(List<String> ids) {
        return projectFunctionRepository.findAllByIdIn(ids);
    }

    @Override
    public List<LoadFunctionResponse> loadFunction(String workspaceId, String projectId) {
        List<LoadFunctionResponse> loadFunctionResponses = customizedFunctionRepository
            .loadFunction(null, workspaceId, GlobalFunctionEntity.class);
        List<LoadFunctionResponse> results = new ArrayList<>(loadFunctionResponses);
        results.addAll(customizedFunctionRepository
            .loadFunction(projectId, null, ProjectFunctionEntity.class));
        return results;
    }

    private void publishEvent(OperationType type, List<ProjectFunctionEntity> entityList) {
        applicationEventPublisher.publishEvent(new EngineProjectFunctionEvent(entityList, type));
    }

}