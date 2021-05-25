package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.GLOBAL_ENV;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static com.sms.satp.common.exception.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.mapper.GlobalEnvironmentMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.GlobalEnvironmentRepository;
import com.sms.satp.service.GlobalEnvironmentService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalEnvironmentServiceImpl implements GlobalEnvironmentService {

    private final GlobalEnvironmentRepository globalEnvironmentRepository;
    private final GlobalEnvironmentMapper globalEnvironmentMapper;
    private final CommonDeleteRepository commonDeleteRepository;

    public GlobalEnvironmentServiceImpl(GlobalEnvironmentRepository globalEnvironmentRepository,
        GlobalEnvironmentMapper globalEnvironmentMapper,
        CommonDeleteRepository commonDeleteRepository) {
        this.globalEnvironmentRepository = globalEnvironmentRepository;
        this.globalEnvironmentMapper = globalEnvironmentMapper;
        this.commonDeleteRepository = commonDeleteRepository;
    }

    @Override
    public GlobalEnvironmentResponse findById(String id) {
        return globalEnvironmentRepository.findById(id).map(globalEnvironmentMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR));
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = GLOBAL_ENV,
        template = "{{#globalEnvironmentRequest.envName}}")
    public Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-add()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            globalEnvironmentRepository.insert(globalEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = GLOBAL_ENV,
        template = "{{#globalEnvironmentRequest.envName}}")
    public Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-edit()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            boolean exists = globalEnvironmentRepository.existsById(globalEnvironmentRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "GlobalEnvironment", globalEnvironmentRequest.getId());
            }
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            globalEnvironmentRepository.save(globalEnvironment);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<GlobalEnvironmentResponse> list() {
        try {
            List<GlobalEnvironment> globalEnvironments = globalEnvironmentRepository
                .findByRemovedOrderByCreateDateTimeDesc(Boolean.FALSE);
            return globalEnvironmentMapper.toDtoList(globalEnvironments);
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = GLOBAL_ENV,
        template = "{{#result?.![#this.envName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonDeleteRepository.deleteByIds(ids, GlobalEnvironment.class);
        } catch (Exception e) {
            log.error("Failed to delete the GlobalEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID);
        }
    }
}
