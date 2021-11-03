package com.sms.courier.parser.impl;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;

public class CoverApiImportHandler extends AbstractApiImportHandler {

    @Override
    public void handle(List<ApiEntity> apiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus,
        ProjectImportFlowEntity projectImportFlowEntity) {
        if (MapUtils.isNotEmpty(oldApiEntities)) {
            // Delete all old api
            deleteInvalidApi(oldApiEntities.values(), applicationContext, projectImportFlowEntity);
        }
        // Record new api.
        apiEntities.forEach(apiEntity -> recordAddApi(apiEntity, projectImportFlowEntity));

        saveDiffApiEntities(apiEntities, applicationContext);
    }

}
