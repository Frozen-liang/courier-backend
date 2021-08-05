package com.sms.courier.parser;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public interface DiffApiEntitiesFactory {

    Collection<ApiEntity> build(List<ApiEntity> newApiEntities,
        Map<String, ApiEntity> oldApiEntities, ApplicationContext applicationContext, ApiStatus apiChangeStatus);

}
