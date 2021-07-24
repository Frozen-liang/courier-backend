package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.GLOBAL_FUNCTION;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_LIST_ERROR;
import static com.sms.satp.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonField.REMOVE;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.entity.function.GlobalFunctionEntity;
import com.sms.satp.mapper.GlobalFunctionMapper;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.GlobalFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import com.sms.satp.utils.ExceptionUtils;
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
public class GlobalFunctionServiceImpl implements GlobalFunctionService {

    private final GlobalFunctionRepository globalFunctionRepository;
    private final GlobalFunctionMapper globalFunctionMapper;
    private final CommonRepository commonRepository;
    private static final String FUNCTION_KEY = "functionKey";

    public GlobalFunctionServiceImpl(GlobalFunctionRepository globalFunctionRepository,
        GlobalFunctionMapper globalFunctionMapper,
        CommonRepository commonRepository) {
        this.globalFunctionRepository = globalFunctionRepository;
        this.globalFunctionMapper = globalFunctionMapper;
        this.commonRepository = commonRepository;
    }

    @Override
    public GlobalFunctionResponse findById(String id) {
        return globalFunctionRepository.findById(id).map(globalFunctionMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_GLOBAL_FUNCTION_BY_ID_ERROR));
    }

    @Override
    public List<GlobalFunctionResponse> list(String workspaceId, String functionKey, String functionName) {
        try {
            GlobalFunctionEntity globalFunction = GlobalFunctionEntity.builder().workspaceId(workspaceId)
                .functionKey(functionKey)
                .functionName(functionName).build();
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(REMOVE.getName(), ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(FUNCTION_KEY, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<GlobalFunctionEntity> example = Example.of(globalFunction, matcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
            return globalFunctionMapper.toDtoList(globalFunctionRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the GlobalFunction list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_FUNCTION_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = GLOBAL_FUNCTION,
        template = "{{#globalFunctionRequest.functionName}}")
    public Boolean add(GlobalFunctionRequest globalFunctionRequest) {
        log.info("GlobalFunctionService-add()-params: [GlobalFunction]={}", globalFunctionRequest.toString());
        try {
            GlobalFunctionEntity globalFunction = globalFunctionMapper.toEntity(globalFunctionRequest);
            globalFunctionRepository.insert(globalFunction);
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = GLOBAL_FUNCTION,
        template = "{{#globalFunctionRequest.functionName}}")
    public Boolean edit(GlobalFunctionRequest globalFunctionRequest) {
        log.info("GlobalFunctionService-edit()-params: [GlobalFunction]={}", globalFunctionRequest.toString());
        try {
            boolean exists = globalFunctionRepository.existsById(globalFunctionRequest.getId());

            isTrue(exists, EDIT_NOT_EXIST_ERROR, "GlobalFunction", globalFunctionRequest.getId());
            GlobalFunctionEntity globalFunction = globalFunctionMapper.toEntity(globalFunctionRequest);
            globalFunctionRepository.save(globalFunction);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = GLOBAL_FUNCTION,
        template = "{{#result?.![#this.functionName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, GlobalFunctionEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the GlobalFunction!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
    }

}