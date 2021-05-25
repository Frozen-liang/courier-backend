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
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.dto.response.ProjectFunctionResponse;
import com.sms.satp.entity.function.ProjectFunction;
import com.sms.satp.mapper.ProjectFunctionMapper;
import com.sms.satp.repository.CommonDeleteRepository;
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
    private final CommonDeleteRepository commonDeleteRepository;
    private static final String FUNCTION_KEY = "functionKey";

    public ProjectFunctionServiceImpl(ProjectFunctionRepository projectFunctionRepository,
        ProjectFunctionMapper projectFunctionMapper, GlobalFunctionService globalFunctionService,
        CommonDeleteRepository commonDeleteRepository) {
        this.projectFunctionRepository = projectFunctionRepository;
        this.projectFunctionMapper = projectFunctionMapper;
        this.globalFunctionService = globalFunctionService;
        this.commonDeleteRepository = commonDeleteRepository;
    }

    @Override
    public ProjectFunctionResponse findById(String id) {
        return projectFunctionRepository.findById(id).map(projectFunctionMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_PROJECT_FUNCTION_BY_ID_ERROR));
    }

    @Override
    public List<Object> list(String projectId, String functionKey, String functionName) {
        try {
            ProjectFunction projectFunction = ProjectFunction.builder()
                .projectId(projectId).functionKey(functionKey).functionName(functionName).build();
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getFiled(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(FUNCTION_KEY, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getFiled(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<ProjectFunction> example = Example.of(projectFunction, matcher);
            ArrayList<Object> list = new ArrayList<>();
            List<GlobalFunctionResponse> globalFunctionList = globalFunctionService.list(functionKey, functionName);
            List<ProjectFunction> projectFunctionList = projectFunctionRepository.findAll(example, sort);
            list.addAll(globalFunctionList);
            list.addAll(projectFunctionMapper.toDtoList(projectFunctionList));
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
            ProjectFunction projectFunction = projectFunctionMapper.toEntity(projectFunctionRequest);
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
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ProjectFunction", projectFunctionRequest.getId());
            }
            ProjectFunction projectFunction = projectFunctionMapper.toEntity(projectFunctionRequest);
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
            return commonDeleteRepository.deleteByIds(ids, ProjectFunction.class);
        } catch (Exception e) {
            log.error("Failed to delete the ProjectFunction!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }

}