package com.sms.courier.service.impl;

import static com.sms.courier.utils.Assert.isFalse;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ImportStatus;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.common.listener.event.ApiDeleteEvent;
import com.sms.courier.dto.request.ApiCaseRequest;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiAndCaseResponse;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.entity.project.ApiImportRollbackRecord;
import com.sms.courier.entity.project.ApiRecord;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.entity.structure.ApiStructureRefRecordEntity;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.mapper.ApiMapper;
import com.sms.courier.repository.ApiDataStructureRefRecordRepository;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiService;
import com.sms.courier.service.AsyncService;
import com.sms.courier.service.ProjectImportSourceService;
import com.sms.courier.utils.DateUtil;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.MD5Util;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.webhook.WebhookEvent;
import com.sms.courier.webhook.enums.WebhookType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final ApiHistoryMapper apiHistoryMapper;
    private final CustomizedApiRepository customizedApiRepository;
    private final ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository;
    private final AsyncService asyncService;
    private final ProjectImportSourceService projectImportSourceService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProjectImportFlowRepository projectImportFlowRepository;


    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository,
        AsyncService asyncService,
        ProjectImportSourceService projectImportSourceService,
        ApplicationEventPublisher applicationEventPublisher,
        ProjectImportFlowRepository projectImportFlowRepository) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
        this.apiDataStructureRefRecordRepository = apiDataStructureRefRecordRepository;
        this.asyncService = asyncService;
        this.projectImportSourceService = projectImportSourceService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.projectImportFlowRepository = projectImportFlowRepository;
    }

    @SneakyThrows
    @Override
    public boolean importDocumentByFile(ApiImportRequest apiImportRequest) {
        String content = IOUtils.toString(apiImportRequest.getFile().getInputStream(), StandardCharsets.UTF_8);
        asyncService.importApi(apiMapper.toImportSource(apiImportRequest, content));
        return true;
    }

    @Override
    public Boolean syncApiByProImpSourceIds(List<String> proImpSourceIds) {
        Iterable<ProjectImportSourceEntity> projectImportSources = projectImportSourceService
            .findByIds(proImpSourceIds);
        projectImportSources.forEach(projectImportSource -> {
            asyncService.importApi(apiMapper.toImportSource(projectImportSource));
        });
        return true;
    }

    @Override
    public ApiResponse findById(String id) {
        return customizedApiRepository.findById(id)
            .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_API_BY_ID_ERROR));
    }

    @Override
    public Page<ApiPageResponse> page(ApiPageRequest apiPageRequest) {
        try {
            return customizedApiRepository.page(apiPageRequest);
        } catch (Exception e) {
            log.error("Failed to get the Api page!", e);
            throw new ApiTestPlatformException(ErrorCode.GET_API_PAGE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = OperationType.ADD, operationModule = OperationModule.API,
        template = "{{#apiRequest.apiName}}")
    public Boolean add(ApiRequest apiRequest) {
        log.info("ApiService-add()-params: [Api]={}", apiRequest.toString());
        try {
            String apiHistoryId = ObjectId.get().toString();
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
            apiEntity.setHistoryId(apiHistoryId);
            isFalse(apiRepository.existsByProjectIdAndApiPathAndRequestMethod(apiRequest.getProjectId(),
                apiRequest.getApiPath(), apiRequest.getRequestMethod()), "The same path and request method exist!");
            ApiEntity newApiEntity = apiRepository.insert(apiEntity);
            ApiHistoryEntity apiHistoryEntity = createApiHistory(apiHistoryId,
                StringUtils.defaultIfBlank(apiRequest.getEditDescription(), "First create api!"), newApiEntity);
            // 如果没用引用数据结构 则不需要保存引用关系
            if (CollectionUtils.isNotEmpty(apiRequest.getAddStructIds())) {
                saveRef(newApiEntity.getId(), newApiEntity.getApiName(), apiRequest.getAddStructIds(),
                    apiRequest.getRemoveStructIds());
            }
            apiHistoryRepository.insert(apiHistoryEntity);
            publishWebhookEvent(WebhookType.API_ADD, newApiEntity);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.ADD_API_ERROR);
        }
        return true;
    }

    private ApiHistoryEntity createApiHistory(String id, String description, ApiEntity apiEntity) {
        return ApiHistoryEntity.builder()
            .id(id)
            .description(description)
            .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build();
    }


    @Override
    @LogRecord(operationType = OperationType.EDIT, operationModule = OperationModule.API,
        template = "{{#apiRequest.apiName}}")
    public Boolean edit(ApiRequest apiRequest) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequest.toString());
        try {
            ApiEntity oldApiEntity = apiRepository.findById(apiRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.EDIT_NOT_EXIST_ERROR, "Api", apiRequest.getId()));
            isFalse(!StringUtils.equals(apiRequest.getApiPath() + apiRequest.getRequestMethod(),
                oldApiEntity.getApiPath() + oldApiEntity.getRequestMethod()) && apiRepository
                .existsByProjectIdAndApiPathAndRequestMethod(apiRequest.getProjectId(),
                    apiRequest.getApiPath(), apiRequest.getRequestMethod()), "The same path and request method exist!");
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            apiEntity.setSwaggerId(oldApiEntity.getSwaggerId());
            apiEntity.setCaseCount(oldApiEntity.getCaseCount());
            apiEntity.setSceneCaseCount(oldApiEntity.getSceneCaseCount());
            apiEntity.setOtherProjectSceneCaseCount(oldApiEntity.getOtherProjectSceneCaseCount());
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
            String apiHistoryId = ObjectId.get().toString();
            apiEntity.setHistoryId(apiHistoryId);
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            ApiHistoryEntity apiHistoryEntity = createApiHistory(apiHistoryId,
                StringUtils.defaultIfBlank(apiRequest.getEditDescription(), "Edit api!"), apiEntity);
            saveRef(newApiEntity.getId(), newApiEntity.getApiName(), apiRequest.getAddStructIds(),
                apiRequest.getRemoveStructIds());
            apiHistoryRepository.insert(apiHistoryEntity);
            publishWebhookEvent(WebhookType.API_UPDATE, newApiEntity);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.EDIT_API_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.DELETE, operationModule = OperationModule.API,
        template = "{{#result?.![#this.apiName]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return customizedApiRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.DELETE_API_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = OperationType.REMOVE, operationModule = OperationModule.API,
        template = "{{#result?.![#this.apiName]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("Delete api ids:{}.", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        apiDataStructureRefRecordRepository.deleteAllByIdIn(ids);
        apiRepository.deleteAllByIdIn(ids);
        ApiDeleteEvent apiDeleteEvent = new ApiDeleteEvent(ids);
        applicationEventPublisher.publishEvent(apiDeleteEvent);
        applicationEventPublisher.publishEvent(WebhookEvent.create(WebhookType.API_DELETE, ids));
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.CLEAR_RECYCLE_BIN, operationModule = OperationModule.API)
    public Boolean deleteAll(String projectId) {
        log.info("Delete all api projectId:{}.", projectId);
        List<ApiEntity> apiEntities = apiRepository.deleteAllByProjectIdAndRemovedIsTrue(projectId);
        List<String> ids = apiEntities.stream().map(ApiEntity::getId).collect(Collectors.toList());
        apiDataStructureRefRecordRepository.deleteAllByIdIn(ids);
        ApiDeleteEvent apiDeleteEvent = new ApiDeleteEvent(ids);
        applicationEventPublisher.publishEvent(apiDeleteEvent);
        applicationEventPublisher.publishEvent(WebhookEvent.create(WebhookType.API_DELETE, ids));
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.RECOVER, operationModule = OperationModule.API,
        template = "{{#result?.![#this.apiName]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        return customizedApiRepository.recover(ids);
    }

    @Override
    public Boolean batchUpdateByIds(BatchUpdateByIdRequest<Object> batchUpdateRequest) {
        UpdateRequest<Object> updateRequest = batchUpdateRequest.getUpdateRequest();
        List<String> ids = batchUpdateRequest.getIds();
        try {
            return customizedApiRepository
                .updateFieldByIds(batchUpdateRequest.getIds(), updateRequest, ApiEntity.class);
        } catch (Exception e) {
            log.error("Batch update {} error. ids:{} key:{} value:{} message:{}",
                ids, "Api", updateRequest.getKey(), updateRequest.getValue(), e.getMessage());
            throw ExceptionUtils
                .mpe(ErrorCode.BATCH_UPDATE_ERROR, ids, "Api", updateRequest.getKey(), updateRequest.getValue());
        }
    }

    @Override
    public Boolean resetApiVersion(String historyId) {
        try {
            ApiHistoryEntity apiHistoryEntity = apiHistoryRepository.findById(historyId)
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_INTERFACE_HISTORY_BY_ID_ERROR));
            Optional<ApiEntity> optional = apiRepository.findById(apiHistoryEntity.getRecord().getId());
            if (optional.isPresent()) {
                ApiEntity oldApiEntity = optional.get();
                ApiEntity newApiEntity = apiMapper.toEntityByHistory(apiHistoryEntity.getRecord());
                newApiEntity.setHistoryId(apiHistoryEntity.getId());
                newApiEntity.setCaseCount(oldApiEntity.getCaseCount());
                newApiEntity.setSceneCaseCount(oldApiEntity.getSceneCaseCount());
                newApiEntity.setOtherProjectSceneCaseCount(oldApiEntity.getOtherProjectSceneCaseCount());
                apiRepository.save(newApiEntity);
                publishWebhookEvent(WebhookType.API_UPDATE, newApiEntity);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("Failed to reset api version!", e);
            throw ExceptionUtils.mpe(ErrorCode.RESET_API_VERSION_ERROR);
        }
    }

    @Override
    public List<ApiAndCaseResponse> queryByApiPathAndRequestMethod(String projectId, List<ApiCaseRequest> requests) {
        List<String> apiPaths = requests.stream().map(ApiCaseRequest::getApiPath).collect(Collectors.toList());
        List<Integer> requestMethods = requests.stream().map(ApiCaseRequest::getRequestMethod)
            .collect(Collectors.toList());
        Map<String, List<ApiCaseRequest>> apiCaseRequestMap = requests.stream().collect(
            Collectors.groupingBy(apiCaseRequest -> apiCaseRequest.getApiPath() + apiCaseRequest.getRequestMethod()));
        Map<String, ApiResponse> apiResponseMap = apiRepository
            .findByProjectIdAndApiPathInAndRequestMethodIn(projectId, apiPaths, requestMethods)
            .collect(Collectors.toMap((api) -> api.getApiPath() + api.getRequestMethod(), Function.identity()));
        List<ApiAndCaseResponse> responses = new ArrayList<>();
        for (Entry<String, List<ApiCaseRequest>> entry : apiCaseRequestMap.entrySet()) {
            Optional.ofNullable(apiResponseMap.get(entry.getKey()))
                .ifPresent((api) -> responses.add(ApiAndCaseResponse.builder().apiResponse(api)
                    .apiTestCase(apiMapper.toApiTestCaseResponse(entry.getValue())).build()));
        }
        return responses;
    }

    @Override
    public Boolean rollback(String projectId) {
        ProjectImportFlowEntity importFlow = projectImportFlowRepository
            .findFirstByProjectIdOrderByCreateDateTimeDesc(projectId)
            .orElseThrow(() -> ExceptionUtils.mpe("The import record not exits!"));
        ApiImportRollbackRecord rollbackRecord = importFlow.getRollbackRecord();
        if (Objects.nonNull(rollbackRecord)) {
            throw ExceptionUtils.mpe("%s has been rolled back on %s!",
                rollbackRecord.getUsername(), DateUtil.toString(rollbackRecord.getRollbackTime()));
        }
        isTrue(ImportStatus.SUCCESS == importFlow.getImportStatus(),
            "Only the latest import record can be rolled back if it is in a successful!");
        List<ApiRecord> deletedApi = importFlow.getDeletedApi();
        List<ApiRecord> updatedApi = importFlow.getUpdatedApi();
        List<ApiRecord> addedApi = importFlow.getAddedApi();
        if (deletedApi.isEmpty() && updatedApi.isEmpty() && addedApi.isEmpty()) {
            return false;
        }
        rollbackDeletedApi(deletedApi);
        rollbackUpdatedApi(updatedApi);
        rollbackAddedApi(addedApi);
        saveRollbackRecord(importFlow);
        return Boolean.TRUE;
    }

    private void rollbackDeletedApi(List<ApiRecord> deletedApi) {
        List<String> deleteApiHistoryIds = deletedApi.stream().map(ApiRecord::getHistoryId)
            .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deleteApiHistoryIds)) {
            List<String> deleteApiId = deletedApi.stream().map(ApiRecord::getId)
                .collect(Collectors.toList());
            List<ApiEntity> apiEntities = apiHistoryRepository.findAllByIdIn(deleteApiHistoryIds)
                .map(apiHistoryEntity -> {
                    ApiEntity apiEntity = apiMapper.toEntityByHistory(apiHistoryEntity.getRecord());
                    apiEntity.setHistoryId(apiHistoryEntity.getId());
                    return apiEntity;
                })
                .collect(Collectors.toList());
            apiRepository.insert(apiEntities);
            customizedApiRepository.updateCaseStatus(deleteApiId, ApiBindingStatus.BINDING);
        }
    }

    private void rollbackUpdatedApi(List<ApiRecord> updatedApi) {
        updatedApi.forEach(apiRecord -> this.resetApiVersion(apiRecord.getHistoryId()));
    }

    private void rollbackAddedApi(List<ApiRecord> addedApi) {
        this.deleteByIds(addedApi.stream().map(ApiRecord::getId).collect(Collectors.toList()));
    }

    private void saveRollbackRecord(ProjectImportFlowEntity importFlow) {
        CustomUser currentUser = SecurityUtil.getCurrentUser();
        ApiImportRollbackRecord rollbackRecord = ApiImportRollbackRecord.builder()
            .userId(currentUser.getId())
            .username(currentUser.getUsername())
            .build();
        UpdateRequest<ApiImportRollbackRecord> updateRequest = new UpdateRequest<>();
        updateRequest.setKey("rollbackRecord");
        updateRequest.setValue(rollbackRecord);
        customizedApiRepository
            .updateFieldByIds(List.of(importFlow.getId()), updateRequest, ProjectImportFlowEntity.class);
    }

    private void saveRef(String id, String name, List<String> addStructIds, List<String> removeStructIds) {
        addStructIds = Objects.requireNonNullElse(addStructIds, new ArrayList<>());
        removeStructIds = Objects.requireNonNullElse(removeStructIds, new ArrayList<>());
        ApiStructureRefRecordEntity apiStructureRefRecordEntity = apiDataStructureRefRecordRepository.findById(id)
            .orElse(ApiStructureRefRecordEntity.builder().id(id).refStructIds(new ArrayList<>()).build());
        List<String> refStructIds = apiStructureRefRecordEntity.getRefStructIds();
        refStructIds.addAll(addStructIds);
        if (!refStructIds.isEmpty()) {
            removeStructIds.forEach(refStructIds::remove);
        }
        apiStructureRefRecordEntity.setName(name);
        apiDataStructureRefRecordRepository.save(apiStructureRefRecordEntity);
    }

    private void publishWebhookEvent(WebhookType webhookType, ApiEntity newApiEntity) {
        applicationEventPublisher
            .publishEvent(WebhookEvent.create(webhookType, apiMapper.toWebhookResponse(newApiEntity)));
    }
}
