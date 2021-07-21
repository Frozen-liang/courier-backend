package com.sms.satp.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.ApiRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for CoverApiEntitiesFactory")
public class IncrementApiEntitiesFactoryTest {

    private final IncrementApiEntitiesFactory incrementApiEntitiesFactory = new IncrementApiEntitiesFactory();

    @Test
    public void build_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApiEntity oldApiEntity = ApiEntity.builder().swaggerId("swagger1").build();
        Map<String, ApiEntity> oldApiEntities = Map.of("swagger1", oldApiEntity);
        ApiRepository apiRepository = mock(ApiRepository.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        doNothing().when(apiRepository).deleteAll(any());
        doNothing().when(applicationContext).publishEvent(any());
        Collection<ApiEntity> apiEntities = incrementApiEntitiesFactory.build(newApiEntities, oldApiEntities,
            applicationContext, ApiStatus.DESIGN);
        assertThat(apiEntities).isNotNull();
    }


    private List<ApiEntity> getApi() {
        ArrayList<ApiEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(ApiEntity.builder().id(ObjectId.get().toString()).swaggerId("swagger" + new Random().nextInt(3))
                .apiName("test" + Math.random()).build());
        }
        return list;
    }
}
