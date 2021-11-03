package com.sms.courier.parser;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public interface ApiImportHandler {

    void handle(List<ApiEntity> apiEntities,
        Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext,
        ApiStatus apiChangeStatus,
        ProjectImportFlowEntity projectImportFlowEntity);
}
