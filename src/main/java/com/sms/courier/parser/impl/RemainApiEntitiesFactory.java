package com.sms.courier.parser.impl;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.parser.DiffApiEntitiesFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;

public class RemainApiEntitiesFactory implements DiffApiEntitiesFactory {

    @Override
    public Collection<ApiEntity> build(List<ApiEntity> newApiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus) {
        return newApiEntities.parallelStream()
            .filter(apiEntity -> !oldApiEntities.containsKey(apiEntity.getSwaggerId()))
            .collect(Collectors.toList());
    }
}
