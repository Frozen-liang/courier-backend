package com.sms.courier.parser.impl;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;

public class RemainApiImportHandler extends AbstractApiImportHandler {

    @Override
    public void handle(List<ApiEntity> newApiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus,
        ProjectImportFlowEntity projectImportFlowEntity) {
        newApiEntities = Objects.requireNonNullElse(newApiEntities, Collections.emptyList());
        if (MapUtils.isEmpty(oldApiEntities)) {
            List<ApiEntity> newApiList = newApiEntities.stream()
                // Record new api
                .peek(apiEntity -> recordAddApi(apiEntity, projectImportFlowEntity))
                .collect(Collectors.toList());
            saveDiffApiEntities(newApiList, applicationContext);
            return;
        }
        List<ApiEntity> newApiList = newApiEntities.stream()
            .filter(apiEntity -> !oldApiEntities.containsKey(apiEntity.getSwaggerId()))
            // Record new api
            .peek(apiEntity -> recordAddApi(apiEntity, projectImportFlowEntity))
            .collect(Collectors.toList());
        saveDiffApiEntities(newApiList, applicationContext);
    }
}
