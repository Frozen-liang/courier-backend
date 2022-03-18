package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.API_TEST_CASE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.CASE_SYNC;
import static com.sms.courier.common.enums.OperationType.CLEAR_RECYCLE_BIN;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.RECOVER;
import static com.sms.courier.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.CASE_SYNC_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_CASE_BY_API_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.common.function.FunctionHandler;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCasePageRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateCaseByApiRequest;
import com.sms.courier.dto.request.UpdateCaseByApiRequest.CaseRequest;
import com.sms.courier.dto.response.ApiTestCasePageResponse;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.CaseApiCountHandler;
import com.sms.courier.utils.ExceptionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiTestCaseServiceImpl extends AbstractCaseService implements ApiTestCaseService {

    private final ApiTestCaseRepository apiTestCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final CaseApiCountHandler caseApiCountHandler;

    public ApiTestCaseServiceImpl(ApiTestCaseRepository apiTestCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        ApiTestCaseMapper apiTestCaseMapper, CaseApiCountHandler caseApiCountHandler,
        ApiRepository apiRepository, ParamInfoMapper paramInfoMapper) {
        super(apiRepository, paramInfoMapper);
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.caseApiCountHandler = caseApiCountHandler;
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
            throw ExceptionUtils.mpe(GET_API_TEST_CASE_LIST_ERROR);
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
            caseApiCountHandler.addTestCaseByApiIds(List.of(apiTestCase.getApiEntity().getId()), 1);
        } catch (Exception e) {
            log.error("Failed to add the ApiTestCase!", e);
            throw ExceptionUtils.mpe(ADD_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }


    @Override
    @LogRecord(operationType = EDIT, operationModule = API_TEST_CASE,
        template = "{{#apiTestCaseRequest.caseName}}", sourceId = "{{#apiTestCaseRequest.id}}")
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
            throw ExceptionUtils.mpe(EDIT_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = API_TEST_CASE,
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            Boolean result = customizedApiTestCaseRepository.deleteByIds(ids);
            FunctionHandler.confirmed(result, ids).handler(this::deleteApiCaseCount);
            return result;
        } catch (Exception e) {
            log.error("Failed to delete the ApiTestCase!", e);
            throw ExceptionUtils.mpe(DELETE_API_TEST_CASE_BY_ID_ERROR);
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
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
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
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean recover(List<String> ids) {
        Boolean isSuccess = customizedApiTestCaseRepository.recover(ids);
        FunctionHandler.confirmed(isSuccess, ids).handler(this::addApiCaseCount);
        return isSuccess;
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

    @Override
    public Long countByProjectIds(List<String> projectIds, LocalDateTime dateTime) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return 0L;
        }
        return customizedApiTestCaseRepository.countByProjectIds(projectIds, dateTime);
    }

    @Override
    public Page<ApiTestCaseResponse> getCasePageByProjectIdsAndCreateDate(List<String> projectIds,
        LocalDateTime dateTime, PageDto pageDto) {
        return customizedApiTestCaseRepository.getCasePageByProjectIdsAndCreateDate(projectIds, dateTime, pageDto);
    }

    @Override
    public Page<ApiTestCasePageResponse> page(ApiTestCasePageRequest request) {
        return customizedApiTestCaseRepository.page(request);
    }

    @Override
    public Boolean updateCaseByApi(List<UpdateCaseByApiRequest> requests) {
        try {
            Map<String, ApiRequest> apiRequestMap = new HashMap<>();
            Map<String, Boolean> isReplaceMap = new HashMap<>();
            Map<Integer, List<String>> caseCountMap = new HashMap<>();
            requests.forEach(request -> {
                List<CaseRequest> caseList = request.getCaseList();
                caseList.forEach(e -> {
                        apiRequestMap.put(e.getId(), request.getApi());
                        isReplaceMap.put(e.getId(), e.isReplace());
                    }
                );
                caseCountMap.compute(caseList.size(), (key, value) -> {
                    List<String> apiIds = Objects.requireNonNullElse(value, new ArrayList<>());
                    apiIds.add(request.getApi().getId());
                    return apiIds;
                });
            });
            List<ApiTestCaseEntity> apiTestCaseEntities = apiTestCaseRepository.findByIdIn(apiRequestMap.keySet());
            apiTestCaseEntities.forEach(apiTestCase -> updateCaseEntity(apiTestCase, isReplaceMap, apiRequestMap));
            apiTestCaseRepository.saveAll(apiTestCaseEntities);
            caseCountMap.forEach((key, value) -> {
                caseApiCountHandler.addTestCaseByApiIds(value, key);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Update case by api error!", e);
            throw ExceptionUtils.mpe(UPDATE_CASE_BY_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = CASE_SYNC, operationModule = API_TEST_CASE,
        template = "{{#request.caseName}}", sourceId = "{{#request.caseId}}")
    public Boolean syncApi(SyncApiRequest request) {
        try {
            ApiTestCaseEntity apiTestCase =
                apiTestCaseRepository.findById(request.getCaseId()).orElseThrow(() -> ExceptionUtils.mpe(
                    ErrorCode.EDIT_NOT_EXIST_ERROR));
            apiTestCase.setLastTestResult(null);
            syncApiEntity(apiTestCase.getApiEntity());
            apiTestCaseRepository.save(apiTestCase);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException courierException) {
            throw courierException;
        } catch (Exception e) {
            log.error("Sync api error!", e);
            throw ExceptionUtils.mpe(CASE_SYNC_API_ERROR);
        }
    }

    public void updateCaseEntity(ApiTestCaseEntity apiTestCase, Map<String, Boolean> isReplaceMap, Map<String,
        ApiRequest> apiRequestMap) {
        ApiRequest apiRequest = apiRequestMap.get(apiTestCase.getId());
        apiTestCase.setStatus(ApiBindingStatus.BINDING);
        apiTestCase.setLastTestResult(null);
        if (isReplaceMap.getOrDefault(apiTestCase.getId(), false)) {
            apiTestCase.setApiEntity(apiTestCaseMapper.toApiEntity(apiRequest));
            return;
        }
        ApiEntity apiEntity = Objects.requireNonNullElse(apiTestCase.getApiEntity(), new ApiEntity());
        apiEntity.setId(apiRequest.getId());
        apiEntity.setApiName(apiRequest.getApiName());
        apiEntity.setApiProtocol(apiRequest.getApiProtocol());
        apiEntity.setApiPath(apiRequest.getApiPath());
        apiEntity.setRequestMethod(apiRequest.getRequestMethod());
        apiEntity.setGroupId(apiRequest.getGroupId());
        apiEntity.setTagId(apiRequest.getTagId());
        apiEntity.setApiStatus(apiRequest.getApiStatus());
        apiEntity.setDescription(apiRequest.getDescription());
        apiTestCase.setApiEntity(apiEntity);
    }

    private void deleteApiCaseCount(List<String> ids) {
        List<String> apiIds = customizedApiTestCaseRepository.findApiIdsByTestIds(ids);
        caseApiCountHandler.deleteTestCaseByApiIds(apiIds);
    }

    private void addApiCaseCount(List<String> ids) {
        List<String> apiIds = customizedApiTestCaseRepository.findApiIdsByTestIds(ids);
        caseApiCountHandler.addTestCaseByApiIds(apiIds, 1);
    }

}