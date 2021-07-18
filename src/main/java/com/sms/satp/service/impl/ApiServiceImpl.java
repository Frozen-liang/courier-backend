package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.API;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.CLEAR_RECYCLE_BIN;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.RECOVER;
import static com.sms.satp.common.enums.OperationType.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ApiHistoryMapper;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.service.AsyncService;
import com.sms.satp.service.ProjectImportSourceService;
import com.sms.satp.utils.ExceptionUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
    private final AsyncService asyncService;
    private final ProjectImportSourceService projectImportSourceService;


    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        AsyncService asyncService,
        ProjectImportSourceService projectImportSourceService) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
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
    /* private void importApi(ImportSourceVo importSource) {
        String projectId = importSource.getProjectId();
        DocumentType documentType = importSource.getDocumentType();
        DocumentDefinition definition = importSource.getDocumentDefinition();
        SaveMode saveMode = importSource.getSaveMode();
        final ProjectImportFlowEntity projectImportFlowEntity = projectImportFlowRepository.save(
            ProjectImportFlowEntity.builder().projectId(projectId).startTime(LocalDateTime.now())
                .build());
        log.info("The project whose Id is [{}] starts to import API documents.", projectId);
        ApiDocumentTransformer<?> transformer = documentType.getTransformer();
        Set<ApiGroupEntity> apiGroupEntities = transformer.toApiGroupEntities(definition,
            (apiGroupEntity -> apiGroupEntity.setProjectId(projectId)));
        // Get all api group.
        Map<String, String> groupMapping = updateGroupIfNeed(projectId, apiGroupEntities);

        List<ApiEntity> apiEntities = transformer.toApiEntities(definition, apiEntity -> {
            apiEntity.setProjectId(projectId);
            apiEntity.setApiStatus(importSource.getApiPresetStatus());
            // Replace the group name with the group id.
            apiEntity.setGroupId(groupMapping.get(apiEntity.getGroupId()));
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
        });

        List<ApiDocumentChecker> apiDocumentCheckers = documentType.getApiDocumentCheckers();

        if (isAllCheckPass(projectImportFlowEntity, apiEntities, apiDocumentCheckers)) {
            Collection<ApiEntity> diffApiEntities = apiEntities;
            // Get old api by project id and swagger id is not empty.
            Map<String, ApiEntity> oldApiEntities = apiRepository
                .findApiEntitiesByProjectIdAndSwaggerIdNotNull(projectId).stream()
                .collect(Collectors.toConcurrentMap(ApiEntity::getSwaggerId, Function.identity()));

            if (MapUtils.isNotEmpty(oldApiEntities)) {
                // Create different api entity by save mode.
                try {
                    diffApiEntities = buildDiffApiEntitiesBySaveMode(apiEntities, oldApiEntities, saveMode);
                } catch (ApiTestPlatformException e) {
                    log.error(e.getMessage());
                    projectImportFlowEntity.setImportStatus(ImportStatus.FAILED);
                    projectImportFlowEntity.setEndTime(LocalDateTime.now());
                    projectImportFlowRepository.save(projectImportFlowEntity);
                    return;
                }
            }

            // Save different api.
            updateApiEntitiesIfNeed(projectId, diffApiEntities);
            projectImportFlowEntity.setImportStatus(ImportStatus.SUCCESS);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());
            projectImportFlowRepository.save(projectImportFlowEntity);
        }

    }

    private Collection<ApiEntity> buildDiffApiEntitiesBySaveMode(List<ApiEntity> newApiEntities,
        Map<String, ApiEntity> oldApiEntities, SaveMode saveMode) {

        if (saveMode == COVER) {
            // Delete all old api.
            apiRepository.deleteAll(oldApiEntities.values());
            // Publish delete event. Update case status.
            applicationEventPublisher.publishEvent(new ApiDeleteEvent(this,
                oldApiEntities.values().parallelStream().map(BaseEntity::getId)
                    .collect(Collectors.toList())));
            return newApiEntities;
        }
        if (saveMode == REMAIN) {
            // Get new api.
            return getNewApiEntities(newApiEntities, oldApiEntities);
        }

        if (saveMode == INCREMENT) {
            // Remove invalid api.
            removeInvalidApi(newApiEntities, oldApiEntities);
            // Get different api.
            return compareApiEntities(newApiEntities, oldApiEntities);
        }

        throw ExceptionUtils.mpe("The save mode must not be null");
    }

    private Collection<ApiEntity> getNewApiEntities(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApis) {
        return apiEntities.parallelStream().filter(apiEntity -> !oldApis.containsKey(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
    }

    private void updateApiEntitiesIfNeed(String projectId, Collection<ApiEntity> diffApiEntities) {
        if (CollectionUtils.isEmpty(diffApiEntities)) {
            log.debug("The project whose Id is [{}],Update API documents in total [0].", projectId);
            return;
        }
        List<ApiHistoryEntity> apiHistoryEntities = apiRepository.saveAll(diffApiEntities).stream()
            .map(apiEntity -> ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build())
            .collect(Collectors.toList());

        apiHistoryRepository.insert(apiHistoryEntities);
        if (log.isDebugEnabled()) {
            log.debug("The project whose Id is [{}],Update API documents in total [{}].",
                projectId, diffApiEntities.size());
        }
    }

    private Collection<ApiEntity> compareApiEntities(List<ApiEntity> apiEntities,
        Map<String, ApiEntity> oldApiEntities) {
        Collection<ApiEntity> diffApiEntities;
        diffApiEntities = CollectionUtils.subtract(apiEntities, oldApiEntities.values());
        diffApiEntities.parallelStream()
            .filter(apiEntity -> oldApiEntities.containsKey(apiEntity.getSwaggerId()))
            .forEach(apiEntity -> {
                ApiEntity oldApiEntity = oldApiEntities.get(apiEntity.getSwaggerId());
                apiEntity.setId(oldApiEntity.getId());
                apiEntity.setApiStatus(oldApiEntity.getApiStatus());
                apiEntity.setPreInject(oldApiEntity.getPreInject());
                apiEntity.setPostInject(oldApiEntity.getPostInject());
                apiEntity.setTagId(oldApiEntity.getTagId());
                apiEntity.setCreateUserId(oldApiEntity.getCreateUserId());
                apiEntity.setCreateDateTime(oldApiEntity.getCreateDateTime());
            });
        return diffApiEntities;
    }

    private void removeInvalidApi(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApiEntities) {
        List<String> swaggerIds =
            apiEntities.stream().map(ApiEntity::getSwaggerId).collect(Collectors.toList());
        Predicate<String> existSwaggerId = swaggerIds::contains;
        // Get invalid api.
        Collection<ApiEntity> invalidApiEntities = oldApiEntities.values().stream()
            .filter(apiEntity -> existSwaggerId.negate().test(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
        log.info("Remove expired API=[{}]",
            invalidApiEntities.stream().map(ApiEntity::getApiPath).collect(Collectors.joining(",")));
        // Delete invalid api.
        apiRepository.deleteAll(invalidApiEntities);
        // Publish delete event. Update case status.
        applicationEventPublisher.publishEvent(new ApiDeleteEvent(this,
            invalidApiEntities.stream().map(BaseEntity::getId).collect(Collectors.toList())));
    }

    private boolean isAllCheckPass(ProjectImportFlowEntity projectImportFlowEntity, List<ApiEntity> apiEntities,
        List<ApiDocumentChecker> apiDocumentCheckers) {
        boolean allCheckPass = apiDocumentCheckers.stream()
            .allMatch(apiDocumentChecker -> apiDocumentChecker
                .check(apiEntities, projectImportFlowEntity, this.applicationContext));
        return allCheckPass;
    }

    private Map<String, String> updateGroupIfNeed(String projectId, Set<ApiGroupEntity> apiGroupEntities) {
        // Get all old group by project id.
        List<ApiGroupEntity> oldGroupEntities =
            apiGroupRepository.findApiGroupEntitiesByProjectId(projectId);
        Collection<ApiGroupEntity> unsavedGroupEntities = CollectionUtils
            .subtract(apiGroupEntities, oldGroupEntities);
        // Save new api group.
        List<ApiGroupEntity> newApiGroupEntities = apiGroupRepository.saveAll(unsavedGroupEntities);
        newApiGroupEntities.addAll(oldGroupEntities);

        return newApiGroupEntities.stream()
            .collect(Collectors.toMap(ApiGroupEntity::getName, ApiGroupEntity::getId));
    }*/


    @Override
    public ApiResponse findById(String id) {

        return customizedApiRepository.findById(id).orElseThrow(() -> ExceptionUtils.mpe(GET_API_BY_ID_ERROR));
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        try {
            return customizedApiRepository.page(apiPageRequest);
        } catch (Exception e) {
            log.error("Failed to get the Api page!", e);
            throw new ApiTestPlatformException(GET_API_PAGE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API, template = "{{#apiRequestDto.apiName}}")
    public Boolean add(ApiRequest apiRequestDto) {
        log.info("ApiService-add()-params: [Api]={}", apiRequestDto.toString());
        try {
            ApiEntity apiEntity = apiMapper.toEntity(apiRequestDto);
            ApiEntity newApiEntity = apiRepository.insert(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ADD_API_ERROR);
        }
        return true;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = API, template = "{{#apiRequestDto.apiName}}")
    public Boolean edit(ApiRequest apiRequest) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequest.toString());
        try {
            boolean exists = apiRepository.existsById(apiRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "Api", apiRequest.getId());
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(EDIT_API_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return customizedApiRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the Api!", e);
            throw new ApiTestPlatformException(DELETE_API_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("Delete api ids:{}.", ids);
        apiRepository.deleteAllByIdIn(ids);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = CLEAR_RECYCLE_BIN, operationModule = API)
    public Boolean deleteAll() {
        log.info("Delete all api when removed is true.");
        apiRepository.deleteAllByRemovedIsTrue();
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = RECOVER, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        return customizedApiRepository.recover(ids);
    }

}
