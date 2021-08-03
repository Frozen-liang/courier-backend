package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.GLOBAL_FUNCTION;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_GLOBAL_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_GLOBAL_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_FUNCTION_KEY_EXIST_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.utils.Assert.isFalse;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.GlobalFunctionRequest;
import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.entity.function.FunctionMessage;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.mapper.GlobalFunctionMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.GlobalFunctionRepository;
import com.sms.courier.service.GlobalFunctionService;
import com.sms.courier.service.MessageService;
import com.sms.courier.utils.ExceptionUtils;
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
public class GlobalFunctionServiceImpl implements GlobalFunctionService {

    private final GlobalFunctionRepository globalFunctionRepository;
    private final GlobalFunctionMapper globalFunctionMapper;
    private final CommonRepository commonRepository;
    private final MessageService messageService;
    private static final String FUNCTION_KEY = "functionKey";

    public GlobalFunctionServiceImpl(GlobalFunctionRepository globalFunctionRepository,
        GlobalFunctionMapper globalFunctionMapper,
        CommonRepository commonRepository, MessageService messageService) {
        this.globalFunctionRepository = globalFunctionRepository;
        this.globalFunctionMapper = globalFunctionMapper;
        this.commonRepository = commonRepository;
        this.messageService = messageService;
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
            String functionKey = globalFunction.getFunctionKey();
            boolean exists = globalFunctionRepository
                .existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(functionKey, globalFunction.getWorkspaceId());
            isFalse(exists, THE_FUNCTION_KEY_EXIST_ERROR, functionKey, "GlobalFunction");
            globalFunctionRepository.insert(globalFunction);
            sendMessageToEngine(List.of(globalFunction.getId()), ADD, globalFunction.getWorkspaceId());
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
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
            GlobalFunctionEntity oldGlobalFunction = globalFunctionRepository.findById(globalFunctionRequest.getId())
                .orElseThrow(
                    () -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "GlobalFunction", globalFunctionRequest.getId()));
            GlobalFunctionEntity globalFunction = globalFunctionMapper.toEntity(globalFunctionRequest);
            String functionKey = globalFunction.getFunctionKey();
            isFalse(!oldGlobalFunction.getFunctionKey().equals(functionKey)
                    && globalFunctionRepository
                    .existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(functionKey, globalFunction.getWorkspaceId()),
                THE_FUNCTION_KEY_EXIST_ERROR, functionKey, "GlobalFunction");
            globalFunctionRepository.save(globalFunction);
            sendMessageToEngine(List.of(globalFunction.getId()), EDIT, globalFunction.getWorkspaceId());
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
            String workspaceId = globalFunctionRepository.findById(ids.get(0)).map(GlobalFunctionEntity::getWorkspaceId)
                .orElse(null);
            Boolean result = commonRepository.deleteByIds(ids, GlobalFunctionEntity.class);
            sendMessageToEngine(ids, DELETE, workspaceId);
            return result;
        } catch (Exception e) {
            log.error("Failed to delete the GlobalFunction!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public Map<String, List<GlobalFunctionResponse>> findAll() {
        return globalFunctionRepository.findAllByRemovedIsFalse()
            .collect(Collectors.groupingBy(GlobalFunctionResponse::getWorkspaceId));
    }

    @Override
    public List<GlobalFunctionResponse> pullFunction(List<String> ids) {
        return globalFunctionRepository.findAllByIdIn(ids);
    }

    private void sendMessageToEngine(List<String> ids, OperationType operationType, String workspaceId) {
        FunctionMessage functionMessage = FunctionMessage.builder()
            .ids(ids)
            .global(true)
            .operationType(operationType.getCode())
            .key(workspaceId)
            .build();
        messageService.enginePullFunctionMessage(functionMessage);
    }

}