package com.sms.courier.parser.impl;

import static org.mockito.ArgumentMatchers.any;
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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for CoverApiEntitiesFactory")
public class RemainApiEntitiesFactoryTest {

    private final RemainApiImportHandler remainApiEntitiesFactory = new RemainApiImportHandler();

    @Test
    public void build_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApiEntity oldApiEntity = ApiEntity.builder().build();
        Map<String, ApiEntity> oldApiEntities = Map.of("client_contoller", oldApiEntity);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
        when(applicationContext.getBean(ApiHistoryMapper.class)).thenReturn(new ApiHistoryMapperImpl());
        when(applicationContext.getBean(ApiHistoryRepository.class)).thenReturn(apiHistoryRepository);
        ApiRepository apiRepository = mock(ApiRepository.class);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        remainApiEntitiesFactory
            .handle(newApiEntities, oldApiEntities, applicationContext, ApiStatus.DESIGN,
                ProjectImportFlowEntity.builder().build());
        verify(apiHistoryRepository, times(1)).insert(any(Iterable.class));

    }

    @Test
    public void build_oldApiIsEmpty_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
        when(applicationContext.getBean(ApiHistoryMapper.class)).thenReturn(new ApiHistoryMapperImpl());
        when(applicationContext.getBean(ApiHistoryRepository.class)).thenReturn(apiHistoryRepository);
        ApiRepository apiRepository = mock(ApiRepository.class);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        remainApiEntitiesFactory
            .handle(newApiEntities, null, applicationContext, ApiStatus.DESIGN,
                ProjectImportFlowEntity.builder().build());
        verify(apiHistoryRepository, times(1)).insert(any(Iterable.class));

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
