package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.PROJECT;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.DocumentFileType;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.ImportStatus;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.listener.event.ApiDeleteEvent;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.mapper.ApiHistoryMapper;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.parser.ApiDocumentChecker;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.MD5Util;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService, ApplicationContextAware {

    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final ApiHistoryMapper apiHistoryMapper;
    private final CustomizedApiRepository customizedApiRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private ApplicationContext applicationContext;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        ApiGroupRepository apiGroupRepository,
        ProjectImportFlowRepository projectImportFlowRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
        this.apiGroupRepository = apiGroupRepository;
        this.projectImportFlowRepository = projectImportFlowRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @SneakyThrows
    @Override
    public boolean importDocument(ApiImportRequest apiImportRequest) {
        String content = IOUtils.toString(apiImportRequest.getFile().getInputStream(), StandardCharsets.UTF_8);
        String projectId = apiImportRequest.getProjectId();

        DocumentType documentType = DocumentFileType.getType(apiImportRequest.getDocumentFileType()).getDocumentType();
        DocumentReader reader = documentType.getReader();
        DocumentDefinition definition = reader.read(content);
        final ProjectImportFlowEntity projectImportFlowEntity = projectImportFlowRepository.save(
            ProjectImportFlowEntity.builder().projectId(projectId).startTime(LocalDateTime.now())
                .build());
        log.info("The project whose Id is [{}] starts to import API documents.", projectId);
        ApiDocumentTransformer<?> transformer = documentType.getTransformer();
        Set<ApiGroupEntity> apiGroupEntities = transformer.toApiGroupEntities(definition,
            (apiGroupEntity -> apiGroupEntity.setProjectId(projectId)));
        Map<String, String> groupMapping = updateGroupIfNeed(projectId, apiGroupEntities);

        List<ApiEntity> apiEntities = transformer.toApiEntities(definition, apiEntity -> {
            apiEntity.setProjectId(projectId);
            apiEntity.setGroupId(groupMapping.get(apiEntity.getGroupId()));
            apiEntity.setMd5(MD5Util.getMD5(apiEntity));
        });

        List<ApiDocumentChecker> apiDocumentCheckers = documentType.getApiDocumentCheckers();

        if (isAllCheckPass(projectImportFlowEntity, apiEntities, apiDocumentCheckers)) {

            Map<String, ApiEntity> oldApiEntities = apiRepository
                .findApiEntitiesByProjectIdAndSwaggerIdNotNull(projectId).stream()
                .collect(Collectors.toConcurrentMap(ApiEntity::getSwaggerId, Function.identity()));

            Collection<ApiEntity> diffApiEntities = apiEntities;
            if (MapUtils.isNotEmpty(oldApiEntities)) {
                diffApiEntities = compareApiEntities(apiEntities, oldApiEntities);
                removeInvalidApi(apiEntities, oldApiEntities);
            }

            updateApiEntitiesIfNeed(projectId, diffApiEntities);
            projectImportFlowEntity.setImportStatus(ImportStatus.SUCCESS);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());
            projectImportFlowRepository.save(projectImportFlowEntity);
        }
        return true;
    }

    private void updateApiEntitiesIfNeed(String projectId, Collection<ApiEntity> diffApiEntities) {
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
        Collection<ApiEntity> invalidApiEntities = oldApiEntities.values().stream()
            .filter(apiEntity -> existSwaggerId.negate().test(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
        log.info("Remove expired API=[{}]",
            invalidApiEntities.stream().map(ApiEntity::getApiPath).collect(Collectors.joining(",")));
        apiRepository.deleteAll(invalidApiEntities);
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
        List<ApiGroupEntity> oldGroupEntities =
            apiGroupRepository.findApiGroupEntitiesByProjectId(projectId);
        Collection<ApiGroupEntity> unsavedGroupEntities = CollectionUtils
            .subtract(apiGroupEntities, oldGroupEntities);
        List<ApiGroupEntity> newApiGroupEntities = apiGroupRepository.saveAll(unsavedGroupEntities);
        newApiGroupEntities.addAll(oldGroupEntities);

        return newApiGroupEntities.stream()
            .collect(Collectors.toMap(ApiGroupEntity::getName, ApiGroupEntity::getId));
    }


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
    @LogRecord(operationType = ADD, operationModule = PROJECT, template = "{{#apiRequestDto.apiName}}")
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
    @LogRecord(operationType = EDIT, operationModule = PROJECT, template = "{{#apiRequestDto.apiName}}")
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
    @LogRecord(operationType = ADD, operationModule = PROJECT, template = "{{#result?.![#this.apiName]}}",
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
    public Boolean deleteByIds(List<String> ids) {
        log.info("Delete api ids:{}.", ids);
        apiRepository.deleteAllByIdIn(ids);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteAll() {
        log.info("Delete all api when removed is true.");
        apiRepository.deleteAllByRemovedIsTrue();
        return Boolean.TRUE;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
