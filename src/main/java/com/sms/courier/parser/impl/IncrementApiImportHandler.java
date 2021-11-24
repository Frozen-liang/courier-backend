package com.sms.courier.parser.impl;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

@Slf4j
public class IncrementApiImportHandler extends AbstractApiImportHandler {

    @Override
    public void handle(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus,
        ProjectImportFlowEntity projectImportFlowEntity) {
        apiEntities = Objects.requireNonNullElse(apiEntities, Collections.emptyList());
        if (MapUtils.isNotEmpty(oldApiEntities)) {
            // Get invalid api.
            Collection<ApiEntity> invalidApi = getInvalidApi(apiEntities, oldApiEntities);
            deleteInvalidApi(invalidApi, applicationContext, projectImportFlowEntity);
        }
        // Get different api.
        Collection<ApiEntity> diffApiList = compareApiEntities(apiEntities, oldApiEntities, apiChangeStatus,
            projectImportFlowEntity);
        saveDiffApiEntities(diffApiList, applicationContext);
    }

    private Collection<ApiEntity> getInvalidApi(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApiEntities) {
        List<String> swaggerIds =
            apiEntities.stream().map(ApiEntity::getSwaggerId).collect(Collectors.toList());
        Predicate<String> existSwaggerId = swaggerIds::contains;
        return oldApiEntities.values().stream()
            .filter(apiEntity -> existSwaggerId.negate().test(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
    }


    private Collection<ApiEntity> compareApiEntities(List<ApiEntity> apiEntities,
        Map<String, ApiEntity> oldApiEntities,
        ApiStatus apiChangeStatus, ProjectImportFlowEntity projectImportFlowEntity) {
        Collection<ApiEntity> diffApiEntities;
        diffApiEntities = CollectionUtils.subtract(apiEntities, oldApiEntities.values());

        diffApiEntities
            .forEach(apiEntity -> this.consumer(apiEntity, oldApiEntities, apiChangeStatus, projectImportFlowEntity));

        return diffApiEntities;
    }

    public void consumer(ApiEntity apiEntity, Map<String, ApiEntity> oldApiEntities, ApiStatus apiChangeStatus,
        ProjectImportFlowEntity projectImportFlowEntity) {

        if (oldApiEntities.containsKey(apiEntity.getSwaggerId())) {
            ApiEntity oldApiEntity = oldApiEntities.get(apiEntity.getSwaggerId());
            apiEntity.setId(oldApiEntity.getId());
            apiEntity.setGroupId(oldApiEntity.getGroupId());
            apiEntity.setApiStatus(Objects.requireNonNullElse(apiChangeStatus, oldApiEntity.getApiStatus()));
            apiEntity.setPreInject(oldApiEntity.getPreInject());
            apiEntity.setPostInject(oldApiEntity.getPostInject());
            apiEntity.setTagId(oldApiEntity.getTagId());
            apiEntity.setCreateUserId(oldApiEntity.getCreateUserId());
            apiEntity.setCreateDateTime(oldApiEntity.getCreateDateTime());
            apiEntity.setCaseCount(oldApiEntity.getCaseCount());
            apiEntity.setSceneCaseCount(oldApiEntity.getSceneCaseCount());
            apiEntity.setOtherProjectSceneCaseCount(oldApiEntity.getOtherProjectSceneCaseCount());
            apiEntity.setApiName(oldApiEntity.getApiName());
            apiEntity.setDescription(oldApiEntity.getDescription());
            apiEntity.setApiProtocol(oldApiEntity.getApiProtocol());
            apiEntity.setApiNodeType(oldApiEntity.getApiNodeType());
            apiEntity.setApiManagerId(oldApiEntity.getApiManagerId());
            apiEntity.setMarkdown(oldApiEntity.getMarkdown());
            apiEntity.setRichText(oldApiEntity.getRichText());
            apiEntity.setHistoryId(ObjectId.get().toString());
            // Record update api
            recordUpdateApi(oldApiEntity, projectImportFlowEntity);
        } else {
            // Record new api
            recordAddApi(apiEntity, projectImportFlowEntity);
        }
    }
}
