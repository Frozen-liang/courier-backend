package com.sms.courier.service.impl;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.entity.structure.ApiStructureRefRecordEntity;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.mapper.ApiMapper;
import com.sms.courier.repository.ApiDataStructureRefRecordRepository;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.ApiService;
import com.sms.courier.service.AsyncService;
import com.sms.courier.service.ProjectImportSourceService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.MD5Util;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Example;
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


    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository,
        AsyncService asyncService,
        ProjectImportSourceService projectImportSourceService) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
        this.apiDataStructureRefRecordRepository = apiDataStructureRefRecordRepository;
        this.asyncService = asyncService;
        this.projectImportSourceService = projectImportSourceService;
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
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
            ApiEntity newApiEntity = apiRepository.insert(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            // 如果没用引用数据结构 则不需要保存引用关系
            if (CollectionUtils.isNotEmpty(apiRequest.getAddStructIds())) {
                saveRef(newApiEntity.getId(), newApiEntity.getApiName(), apiRequest.getAddStructIds(),
                    apiRequest.getRemoveStructIds());
            }
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.ADD_API_ERROR);
        }
        return true;
    }


    @Override
    @LogRecord(operationType = OperationType.EDIT, operationModule = OperationModule.API,
        template = "{{#apiRequest.apiName}}")
    public Boolean edit(ApiRequest apiRequest) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequest.toString());
        try {
            ApiEntity oldApiEntity = apiRepository.findById(apiRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.EDIT_NOT_EXIST_ERROR, "Api", apiRequest.getId()));
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            newApiEntity.setMd5(MD5Util.getMD5(newApiEntity));
            newApiEntity.setCaseCount(oldApiEntity.getCaseCount());
            newApiEntity.setSceneCaseCount(oldApiEntity.getSceneCaseCount());
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            saveRef(newApiEntity.getId(), newApiEntity.getApiName(), apiRequest.getAddStructIds(),
                apiRequest.getRemoveStructIds());
            apiHistoryRepository.insert(apiHistoryEntity);
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
        apiRepository.deleteAllByIdIn(ids);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.CLEAR_RECYCLE_BIN, operationModule = OperationModule.API)
    public Boolean deleteAll() {
        log.info("Delete all api when removed is true.");
        apiRepository.deleteAllByRemovedIsTrue();
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.RECOVER, operationModule = OperationModule.API,
        template = "{{#result?.![#this.apiName]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        return customizedApiRepository.recover(ids);
    }

    @Override
    public Long count(String projectId) {
        return apiRepository.count(Example.of(ApiEntity.builder().projectId(projectId).build()));
    }

    @Override
    public Boolean batchUpdateByIds(BatchUpdateByIdRequest<Object> batchUpdateRequest) {
        UpdateRequest<Object> updateRequest = batchUpdateRequest.getUpdateRequest();
        List<String> ids = batchUpdateRequest.getIds();
        try {
            return customizedApiRepository.updateFieldByIds(batchUpdateRequest.getIds(), updateRequest);
        } catch (Exception e) {
            log.error("Batch update {} error. ids:{} key:{} value:{} message:{}",
                ids, "Api", updateRequest.getKey(), updateRequest.getValue(), e.getMessage());
            throw ExceptionUtils
                .mpe(ErrorCode.BATCH_UPDATE_ERROR, ids, "Api", updateRequest.getKey(), updateRequest.getValue());
        }
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


}
