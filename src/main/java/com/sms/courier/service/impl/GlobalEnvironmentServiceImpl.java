package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.GLOBAL_ENV;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static com.sms.courier.common.exception.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.GlobalEnvironmentRequest;
import com.sms.courier.dto.response.GlobalEnvironmentResponse;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.mapper.GlobalEnvironmentMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.GlobalEnvironmentRepository;
import com.sms.courier.service.GlobalEnvironmentService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalEnvironmentServiceImpl implements GlobalEnvironmentService {

    private final GlobalEnvironmentRepository globalEnvironmentRepository;
    private final GlobalEnvironmentMapper globalEnvironmentMapper;
    private final CommonRepository commonRepository;

    public GlobalEnvironmentServiceImpl(GlobalEnvironmentRepository globalEnvironmentRepository,
        GlobalEnvironmentMapper globalEnvironmentMapper,
        CommonRepository commonRepository) {
        this.globalEnvironmentRepository = globalEnvironmentRepository;
        this.globalEnvironmentMapper = globalEnvironmentMapper;
        this.commonRepository = commonRepository;
    }

    @Override
    public GlobalEnvironmentResponse findById(String id) {
        return globalEnvironmentRepository.findById(id).map(globalEnvironmentMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR));
    }

    @Override
    public GlobalEnvironmentEntity findOne(String id) {
        return globalEnvironmentRepository.findById(id).orElse(null);
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = GLOBAL_ENV,
        template = "{{#globalEnvironmentRequest.envName}}", refId = "workspaceId")
    public Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-add()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            GlobalEnvironmentEntity globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            globalEnvironmentRepository.insert(globalEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = GLOBAL_ENV,
        template = "{{#globalEnvironmentRequest.envName}}", refId = "workspaceId",
        sourceId = "{{#globalEnvironmentRequest.id}}")
    public Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-edit()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            boolean exists = globalEnvironmentRepository.existsById(globalEnvironmentRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "GlobalEnvironment", globalEnvironmentRequest.getId());
            GlobalEnvironmentEntity globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            globalEnvironmentRepository.save(globalEnvironment);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<GlobalEnvironmentResponse> list(String workspaceId) {
        try {
            List<GlobalEnvironmentEntity> globalEnvironments = globalEnvironmentRepository
                .findByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(workspaceId);
            return globalEnvironmentMapper.toDtoList(globalEnvironments);
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = GLOBAL_ENV,
        template = "{{#result?.![#this.envName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"), refId = "workspaceId",
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, GlobalEnvironmentEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the GlobalEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID);
        }
    }
}
