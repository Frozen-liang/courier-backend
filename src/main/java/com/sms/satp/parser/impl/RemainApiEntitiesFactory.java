package com.sms.satp.parser.impl;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.parser.DiffApiEntitiesFactory;
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
