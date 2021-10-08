package com.sms.courier.service.impl;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.DocumentType;
import com.sms.courier.common.enums.ImportStatus;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.enums.SaveMode;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.project.ImportSourceVo;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.mapper.ProjectImportFlowMapper;
import com.sms.courier.parser.ApiDocumentChecker;
import com.sms.courier.parser.ApiDocumentTransformer;
import com.sms.courier.parser.common.DocumentDefinition;
import com.sms.courier.repository.ApiGroupRepository;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ProjectImportFlowRepository;
import com.sms.courier.service.AsyncService;
import com.sms.courier.service.MessageService;
import com.sms.courier.utils.MD5Util;
import com.sms.courier.websocket.Payload;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService, ApplicationContextAware {

    private static final int DEPTH = 1;
    private final DefaultIdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getSharedInstance();
    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiHistoryMapper apiHistoryMapper;
    private final ApiGroupRepository apiGroupRepository;
    private final ProjectImportFlowRepository projectImportFlowRepository;
    private final ProjectImportFlowMapper projectImportFlowMapper;
    private ApplicationContext applicationContext;
    private final MessageService messageService;

    public AsyncServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository,
        ApiHistoryMapper apiHistoryMapper,
        ApiGroupRepository apiGroupRepository,
        ProjectImportFlowRepository projectImportFlowRepository,
        ProjectImportFlowMapper projectImportFlowMapper,
        MessageService messageService) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiHistoryMapper = apiHistoryMapper;
        this.apiGroupRepository = apiGroupRepository;
        this.projectImportFlowRepository = projectImportFlowRepository;
        this.projectImportFlowMapper = projectImportFlowMapper;
        this.messageService = messageService;
    }

    @Override
    @LogRecord(operationType = OperationType.SYNC, operationModule = OperationModule.API)
    @Async
    public void importApi(ImportSourceVo importSource) {
        String projectId = importSource.getProjectId();
        DocumentType documentType = importSource.getDocumentType();
        SaveMode saveMode = importSource.getSaveMode();
        String id = importSource.getId();
        // Only one API source is in sync.
        if (Objects.nonNull(id) && projectImportFlowRepository.existsByIdAndImportStatus(id,
            ImportStatus.RUNNING.getCode())) {
            return;
        }
        final ProjectImportFlowEntity projectImportFlowEntity = projectImportFlowRepository.save(
            ProjectImportFlowEntity.builder().importSourceId(id).projectId(projectId)
                .importStatus(ImportStatus.RUNNING)
                .startTime(LocalDateTime.now())
                .build());
        projectImportFlowRepository.save(projectImportFlowEntity);
        List<ApiGroupEntity> incrementApiGroup = new ArrayList<>();
        try {
            log.info("The project whose Id is [{}] starts to import API documents.", projectId);
            messageService.projectMessage(projectId,
                Payload.ok(projectImportFlowMapper.toProjectImportFlowResponse(projectImportFlowEntity)));

            //Parse swagger or file.
            DocumentDefinition definition = documentType.getReader().read(importSource.getSource());

            ApiDocumentTransformer<?> transformer = documentType.getTransformer();
            Set<ApiGroupEntity> apiGroupEntities = transformer.toApiGroupEntities(definition,
                (apiGroupEntity -> apiGroupEntity.setProjectId(projectId)));
            // Get all api group.
            Map<String, String> groupMapping = updateGroupIfNeed(projectId, apiGroupEntities, incrementApiGroup);

            List<ApiEntity> apiEntities = transformer.toApiEntities(definition, apiEntity -> {
                apiEntity.setProjectId(projectId);
                apiEntity.setApiStatus(importSource.getApiPresetStatus());
                // Replace the group name with the group id.
                apiEntity.setGroupId(groupMapping.get(apiEntity.getGroupId()));
                apiEntity.setMd5(MD5Util.getMD5(apiEntity));
            });

            //Check apiEntities, If check not pass,then throw exception.
            isAllCheckPass(apiEntities, documentType.getApiDocumentCheckers());

            Collection<ApiEntity> diffApiEntities = apiEntities;
            // Get old api by project id and swagger id is not empty.
            Map<String, ApiEntity> oldApiEntities = apiRepository
                .findApiEntitiesByProjectIdAndSwaggerIdNotNull(projectId).stream()
                .collect(Collectors.toConcurrentMap(ApiEntity::getSwaggerId, Function.identity()));

            if (MapUtils.isNotEmpty(oldApiEntities)) {
                // Create different api entity by save mode.
                diffApiEntities = saveMode.getBuildDiffApiEntities().build(apiEntities, oldApiEntities,
                    applicationContext, importSource.getApiChangeStatus());
            }

            // Save different api.
            updateApiEntitiesIfNeed(projectId, diffApiEntities);
            projectImportFlowEntity.setImportStatus(ImportStatus.SUCCESS);
            projectImportFlowEntity.setEndTime(LocalDateTime.now());

        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            importApiErrorHandle(projectImportFlowEntity, incrementApiGroup, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Sync api error.", e);
            importApiErrorHandle(projectImportFlowEntity, incrementApiGroup, ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage());
        }
        projectImportFlowRepository.save(projectImportFlowEntity);
        // Send import message.
        messageService.projectMessage(projectId,
            Payload.ok(projectImportFlowMapper.toProjectImportFlowResponse(projectImportFlowEntity)));
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

    private void isAllCheckPass(List<ApiEntity> apiEntities, List<ApiDocumentChecker> apiDocumentCheckers) {
        apiDocumentCheckers.forEach(apiDocumentChecker -> apiDocumentChecker.check(apiEntities));
    }

    private Map<String, String> updateGroupIfNeed(String projectId, Set<ApiGroupEntity> apiGroupEntities,
        List<ApiGroupEntity> incrementApiGroup) {
        // Get all old group by project id.
        Set<ApiGroupEntity> oldGroupEntities =
            apiGroupRepository.findByProjectIdAndDepth(projectId, DEPTH);
        Collection<ApiGroupEntity> unsavedGroupEntities = CollectionUtils
            .subtract(apiGroupEntities, oldGroupEntities);
        unsavedGroupEntities.forEach((entity) -> {
            Long realGroupId = identifierGenerator.nextId();
            entity.setRealGroupId(realGroupId);
            entity.setPath(Collections.singletonList(realGroupId));
        });

        // Save new api group.
        List<ApiGroupEntity> newApiGroupEntities = apiGroupRepository.saveAll(unsavedGroupEntities);
        incrementApiGroup.addAll(newApiGroupEntities);
        newApiGroupEntities.addAll(oldGroupEntities);

        return newApiGroupEntities.stream()
            .collect(Collectors.toMap(ApiGroupEntity::getName, ApiGroupEntity::getId));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void importApiErrorHandle(ProjectImportFlowEntity projectImportFlowEntity,
        List<ApiGroupEntity> incrementApiGroup, String errorCode, String errorMessage) {
        // If import error. delete increment api group.
        apiGroupRepository.deleteAll(incrementApiGroup);
        projectImportFlowEntity.setImportStatus(ImportStatus.FAILED);
        projectImportFlowEntity.setEndTime(LocalDateTime.now());
        projectImportFlowEntity.setErrorCode(errorCode);
        projectImportFlowEntity.setErrorDetail(errorMessage);
    }
}
