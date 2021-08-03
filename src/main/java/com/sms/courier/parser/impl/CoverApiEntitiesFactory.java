package com.sms.courier.parser.impl;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.listener.event.ApiDeleteEvent;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.parser.DiffApiEntitiesFactory;
import com.sms.courier.repository.ApiRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;

public class CoverApiEntitiesFactory implements DiffApiEntitiesFactory {

    @Override
    public Collection<ApiEntity> build(List<ApiEntity> newApiEntities, Map<String, ApiEntity> oldApiEntities,
        ApplicationContext applicationContext, ApiStatus apiChangeStatus) {
        ApiRepository apiRepository = applicationContext.getBean(ApiRepository.class);

        // Delete all old api.
        apiRepository.deleteAll(oldApiEntities.values());
        // Publish delete event. Update case status.
        applicationContext
            .publishEvent(new ApiDeleteEvent(oldApiEntities.values().parallelStream().map(BaseEntity::getId)
                .collect(Collectors.toList())));
        return newApiEntities;
    }
}
