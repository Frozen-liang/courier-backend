package com.sms.courier.parser.impl;

import com.sms.courier.common.listener.event.ApiDeleteEvent;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.entity.project.ApiRecord;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.parser.ApiImportHandler;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

@Slf4j
public abstract class AbstractApiImportHandler implements ApiImportHandler {

    protected void saveDiffApiEntities(Collection<ApiEntity> diffApiList, ApplicationContext applicationContext) {
        if (CollectionUtils.isEmpty(diffApiList)) {
            return;
        }
        ApiRepository apiRepository = applicationContext.getBean(ApiRepository.class);
        ApiHistoryMapper apiHistoryMapper = applicationContext.getBean(ApiHistoryMapper.class);
        ApiHistoryRepository apiHistoryRepository = applicationContext.getBean(ApiHistoryRepository.class);
        List<ApiHistoryEntity> apiHistoryEntities = apiRepository.saveAll(diffApiList).stream()
            .map(apiEntity -> ApiHistoryEntity.builder()
                .description("Sync api!")
                .record(apiHistoryMapper.toApiHistoryDetail(apiEntity)).build())
            .collect(Collectors.toList());
        apiHistoryRepository.insert(apiHistoryEntities);
    }

    protected void deleteInvalidApi(Collection<ApiEntity> invalidApiList, ApplicationContext applicationContext,
        ProjectImportFlowEntity projectImportFlowEntity) {
        List<String> ids = invalidApiList.stream()
            // Record delete api
            .peek(apiEntity -> recordDeleteApi(apiEntity, projectImportFlowEntity))
            .map(ApiEntity::getId).collect(Collectors.toList());
        ApiRepository apiRepository = applicationContext.getBean(ApiRepository.class);
        // Delete invalid api.
        apiRepository.deleteAllByIdIn(ids);
        // Publish delete event. Update case status.
        applicationContext.publishEvent(new ApiDeleteEvent(ids));
    }

    protected void recordAddApi(ApiEntity apiEntity, ProjectImportFlowEntity projectImportFlowEntity) {
        apiEntity.setId(ObjectId.get().toString());
        apiEntity.setCreateDateTime(LocalDateTime.now());
        apiEntity.setCreateUserId(SecurityUtil.getCurrUserId());
        projectImportFlowEntity.getAddedApi().add(new ApiRecord(apiEntity.getId(), apiEntity.getApiName()));
    }

    protected void recordDeleteApi(ApiEntity apiEntity, ProjectImportFlowEntity projectImportFlowEntity) {
        projectImportFlowEntity.getDeletedApi().add(new ApiRecord(apiEntity.getId(), apiEntity.getApiName()));
    }

    protected void recordUpdateApi(ApiEntity apiEntity, ProjectImportFlowEntity projectImportFlowEntity) {
        projectImportFlowEntity.getUpdatedApi().add(new ApiRecord(apiEntity.getId(), apiEntity.getApiName()));
    }
}
