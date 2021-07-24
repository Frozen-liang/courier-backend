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
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.dto.response.ProjectFunctionResponse;
import com.sms.satp.entity.function.ProjectFunctionEntity;
import com.sms.satp.mapper.ProjectFunctionMapper;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.ProjectFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import com.sms.satp.service.ProjectFunctionService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.ArrayList;
import java.util.List;
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
    private final CommonRepository commonRepository;
    private static final String FUNCTION_KEY = "functionKey";

    public ProjectFunctionServiceImpl(ProjectFunctionRepository projectFunctionRepository,
        ProjectFunctionMapper projectFunctionMapper, GlobalFunctionService globalFunctionService,
        CommonRepository commonRepository) {
        this.projectFunctionRepository = projectFunctionRepository;
        this.projectFunctionMapper = projectFunctionMapper;
        this.globalFunctionService = globalFunctionService;
        this.commonRepository = commonRepository;
    }

    @Override
    public ProjectFunctionResponse findById(String id) {
        return projectFunctionRepository.findById(id).map(projectFunctionMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_FUNCTION_BY_ID_ERROR));
    }

    @Override
    public List<Object> list(String projectId, String workspaceId, String functionKey, String functionName) {
        try {
            List<ProjectFunctionResponse> projectFunctionResponses = this.findAll(projectId, functionKey, functionName);
            ArrayList<Object> list = new ArrayList<>();
            List<GlobalFunctionResponse> globalFunctionList = globalFunctionService.list(workspaceId, functionKey,
                functionName);
            list.addAll(globalFunctionList);
            list.addAll(projectFunctionResponses);
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
            return commonRepository.deleteByIds(ids, ProjectFunctionEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the ProjectFunction!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<ProjectFunctionResponse> findAll(String projectId, String functionKey, String functionName) {
        ProjectFunctionEntity projectFunction = ProjectFunctionEntity.builder()
            .projectId(projectId).functionKey(functionKey).functionName(functionName).build();
        Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withMatcher(PROJECT_ID.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher(FUNCTION_KEY, ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher(REMOVE.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
            .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
        Example<ProjectFunctionEntity> example = Example.of(projectFunction, matcher);
        List<ProjectFunctionEntity> projectFunctionList = projectFunctionRepository.findAll(example, sort);
        return projectFunctionMapper.toDtoList(projectFunctionList);
    }

}