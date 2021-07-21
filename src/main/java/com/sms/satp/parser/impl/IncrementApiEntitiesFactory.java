package com.sms.satp.parser.impl;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.listener.event.ApiDeleteEvent;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.parser.DiffApiEntitiesFactory;
import com.sms.satp.repository.ApiRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;

@Slf4j
public class IncrementApiEntitiesFactory implements DiffApiEntitiesFactory {

    @Override
    public Collection<ApiEntity> build(List<ApiEntity> newApiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus) {
        // Remove invalid api.
        removeInvalidApi(newApiEntities, oldApiEntities, applicationContext);
        // Get different api.
        return compareApiEntities(newApiEntities, oldApiEntities, apiChangeStatus);
    }

    private void removeInvalidApi(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext) {
        List<String> swaggerIds =
            apiEntities.stream().map(ApiEntity::getSwaggerId).collect(Collectors.toList());
        Predicate<String> existSwaggerId = swaggerIds::contains;
        // Get invalid api.
        Collection<ApiEntity> invalidApiEntities = oldApiEntities.values().stream()
            .filter(apiEntity -> existSwaggerId.negate().test(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
        log.info("Remove expired API=[{}]",
            invalidApiEntities.stream().map(ApiEntity::getApiPath).collect(Collectors.joining(",")));
        if (CollectionUtils.isNotEmpty(invalidApiEntities)) {
            ApiRepository apiRepository = applicationContext.getBean(ApiRepository.class);
            // Delete invalid api.
            apiRepository.deleteAll(invalidApiEntities);
            // Publish delete event. Update case status.
            applicationContext.publishEvent(
                new ApiDeleteEvent(invalidApiEntities.stream().map(BaseEntity::getId).collect(Collectors.toList())));
        }
    }


    private Collection<ApiEntity> compareApiEntities(List<ApiEntity> apiEntities,
        Map<String, ApiEntity> oldApiEntities, ApiStatus apiChangeStatus) {
        Collection<ApiEntity> diffApiEntities;
        diffApiEntities = CollectionUtils.subtract(apiEntities, oldApiEntities.values());
        diffApiEntities.parallelStream()
            .filter(apiEntity -> oldApiEntities.containsKey(apiEntity.getSwaggerId()))
            .forEach(apiEntity -> {
                ApiEntity oldApiEntity = oldApiEntities.get(apiEntity.getSwaggerId());
                apiEntity.setId(oldApiEntity.getId());
                apiEntity.setGroupId(oldApiEntity.getGroupId());
                apiEntity.setApiStatus(Objects.requireNonNullElse(apiChangeStatus, oldApiEntity.getApiStatus()));
                apiEntity.setPreInject(oldApiEntity.getPreInject());
                apiEntity.setPostInject(oldApiEntity.getPostInject());
                apiEntity.setTagId(oldApiEntity.getTagId());
                apiEntity.setCreateUserId(oldApiEntity.getCreateUserId());
                apiEntity.setCreateDateTime(oldApiEntity.getCreateDateTime());
            });
        return diffApiEntities;
    }
}
