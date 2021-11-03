package com.sms.courier.parser.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.mapper.ApiHistoryMapperImpl;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for CoverApiEntitiesFactory")
public class IncrementApiEntitiesFactoryTest {

    private final IncrementApiImportHandler incrementApiEntitiesFactory = new IncrementApiImportHandler();

    @Test
    public void build_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApiEntity oldApiEntity = ApiEntity.builder().swaggerId("swagger1").build();
        Map<String, ApiEntity> oldApiEntities = Map.of("swagger1", oldApiEntity);
        ApiRepository apiRepository = mock(ApiRepository.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
        when(applicationContext.getBean(ApiHistoryMapper.class)).thenReturn(new ApiHistoryMapperImpl());
        when(applicationContext.getBean(ApiHistoryRepository.class)).thenReturn(apiHistoryRepository);
        doNothing().when(apiRepository).deleteAllByIdIn(any());
        doNothing().when(applicationContext).publishEvent(any());
        incrementApiEntitiesFactory.handle(newApiEntities, oldApiEntities,
            applicationContext, ApiStatus.DESIGN, ProjectImportFlowEntity.builder().build());
        verify(apiHistoryRepository, times(1)).insert(any(Iterable.class));
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
