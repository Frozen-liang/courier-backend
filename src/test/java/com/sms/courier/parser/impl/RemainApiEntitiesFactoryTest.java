package com.sms.courier.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for CoverApiEntitiesFactory")
public class RemainApiEntitiesFactoryTest {

    private final RemainApiEntitiesFactory remainApiEntitiesFactory = new RemainApiEntitiesFactory();

    @Test
    public void build_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApiEntity oldApiEntity = ApiEntity.builder().build();
        Map<String, ApiEntity> oldApiEntities = Map.of("client_contoller", oldApiEntity);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        Collection<ApiEntity> apiEntities = remainApiEntitiesFactory
            .build(newApiEntities, oldApiEntities, applicationContext, ApiStatus.DESIGN);
        assertThat(apiEntities).isNotNull();
    }


    private List<ApiEntity> getApi() {
        ArrayList<ApiEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(ApiEntity.builder().id(ObjectId.get().toString()).swaggerId("swagger" + Math.random())
                .apiName("test" + Math.random()).build());
        }
        return list;
    }
}
