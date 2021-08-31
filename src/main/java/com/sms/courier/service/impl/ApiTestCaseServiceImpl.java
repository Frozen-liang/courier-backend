package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.CaseType.CASE;
import static com.sms.courier.common.enums.OperationModule.API_TEST_CASE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.CLEAR_RECYCLE_BIN;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.RECOVER;
import static com.sms.courier.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    private final ApiTestCaseRepository apiTestCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ApiTestCaseServiceImpl(ApiTestCaseRepository apiTestCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        ApiTestCaseMapper apiTestCaseMapper,
        ApplicationEventPublisher applicationEventPublisher) {
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public ApiTestCaseResponse findById(String id) {
        return apiTestCaseRepository.findById(id).map(apiTestCaseMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TEST_CASE_BY_ID_ERROR));
    }

    @Override
    public ApiTestCaseEntity findOne(String id) {
        return apiTestCaseRepository.findById(id)
            .orElseThrow(() -> ExceptionUtils.mpe("The apiTestCase not exist. id=%s", id));
    }

    @Override
    public List<ApiTestCaseResponse> list(ObjectId apiId, ObjectId projectId, boolean removed) {
        try {
            return customizedApiTestCaseRepository.listByJoin(apiId, projectId, removed);
        } catch (Exception e) {
            log.error("Failed to get the ApiTestCase list!", e);
            throw new ApiTestPlatformException(GET_API_TEST_CASE_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API_TEST_CASE,
        template = "{{#apiTestCaseRequest.caseName}}")
    public Boolean add(ApiTestCaseRequest apiTestCaseRequest) {
        log.info("ApiTestCaseService-add()-params: [ApiTestCase]={}", apiTestCaseRequest.toString());
        try {
            ApiTestCaseEntity apiTestCase = apiTestCaseMapper.toEntity(apiTestCaseRequest);
            apiTestCaseRepository.insert(apiTestCase);
            AddCaseEvent addCaseEvent = new AddCaseEvent(List.of(apiTestCase.getApiEntity().getId()), CASE);
            applicationEventPublisher.publishEvent(addCaseEvent);
        } catch (Exception e) {
            log.error("Failed to add the ApiTestCase!", e);
            throw new ApiTestPlatformException(ADD_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }


    @Override
    @LogRecord(operationType = EDIT, operationModule = API_TEST_CASE,
        template = "{{#apiTestCaseRequest.caseName}}")
    public Boolean edit(ApiTestCaseRequest apiTestCaseRequest) {
        log.info("ApiTestCaseService-edit()-params: [ApiTestCase]={}", apiTestCaseRequest.toString());
        try {
            boolean exists = apiTestCaseRepository.existsById(apiTestCaseRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ApiTestCase", apiTestCaseRequest.getId());
            ApiTestCaseEntity apiTestCase = apiTestCaseMapper.toEntity(apiTestCaseRequest);
            apiTestCase.setLastTestResult(null);
            apiTestCaseRepository.save(apiTestCase);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the ApiTestCase!", e);
            throw new ApiTestPlatformException(EDIT_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = API_TEST_CASE,
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            Boolean result = customizedApiTestCaseRepository.deleteByIds(ids);
            if (result) {
                DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(ids, CASE);
                applicationEventPublisher.publishEvent(deleteCaseEvent);
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTestCase!", e);
            throw new ApiTestPlatformException(DELETE_API_TEST_CASE_BY_ID_ERROR);
        }
    }

    @Override
    public void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status) {
        log.info("Update ApiTestCase's status to {},apiIds = {}", status.getCode(), apiIds);
        customizedApiTestCaseRepository.updateApiTestCaseStatusByApiId(apiIds, status);
    }

    @Override
    @LogRecord(operationType = OperationType.REMOVE, operationModule = API_TEST_CASE,
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("Delete ApiTestCase ids:{}.", ids);
        apiTestCaseRepository.deleteAllByIdIn(ids);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = CLEAR_RECYCLE_BIN, operationModule = API_TEST_CASE)
    public Boolean deleteAll() {
        log.info("Delete all ApiTestCase when removed is true.");
        apiTestCaseRepository.deleteAllByRemovedIsTrue();
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = RECOVER, operationModule = API_TEST_CASE,
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        return customizedApiTestCaseRepository.recover(ids);
    }

    @Override
    public Long count(String projectId) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("isExecute");
        return apiTestCaseRepository
            .count(Example.of(ApiTestCaseEntity.builder().projectId(projectId).build(), exampleMatcher));
    }

    @Override
    public void insertTestResult(String id, TestResult testResult) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        apiTestCaseRepository.findById(id).ifPresent(apiTestCaseEntity -> {
            log.info("Insert last test result.{}", testResult);
            apiTestCaseEntity.setLastTestResult(testResult);
            apiTestCaseRepository.save(apiTestCaseEntity);
        });
    }

}