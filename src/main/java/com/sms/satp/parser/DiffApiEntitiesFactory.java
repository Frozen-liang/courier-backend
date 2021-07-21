package com.sms.satp.parser;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.entity.api.ApiEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public interface DiffApiEntitiesFactory {

    Collection<ApiEntity> build(List<ApiEntity> newApiEntities,
        Map<String, ApiEntity> oldApiEntities, ApplicationContext applicationContext, ApiStatus apiChangeStatus);

}
