package com.sms.courier.parser.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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
import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for CoverApiEntitiesFactory")
public class CoverApiEntitiesFactoryTest {

    private final CoverApiImportHandler coverApiEntitiesFactory = new CoverApiImportHandler();
    private final static MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(new CustomUser("username", "password",
            Collections.emptyList(), "", "username@qq.com", "nickname", TokenType.USER, LocalDate.now()));
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    public void build_test() {
        List<ApiEntity> newApiEntities = getApi();
        ApiEntity oldApiEntity = ApiEntity.builder().build();
        Map<String, ApiEntity> oldApiEntities = Map.of("client_contoller", oldApiEntity);
        ApiRepository apiRepository = mock(ApiRepository.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
        when(applicationContext.getBean(ApiHistoryMapper.class)).thenReturn(new ApiHistoryMapperImpl());
        when(applicationContext.getBean(ApiHistoryRepository.class)).thenReturn(apiHistoryRepository);
        when(applicationContext.getBean(ApiRepository.class)).thenReturn(apiRepository);
        doNothing().when(apiRepository).deleteAllByIdIn(any());
        doNothing().when(applicationContext).publishEvent(any());
        coverApiEntitiesFactory
            .handle(newApiEntities, oldApiEntities, applicationContext, ApiStatus.DESIGN,
                ProjectImportFlowEntity.builder().build());
        verify(apiRepository, times(1)).deleteAllByIdIn(any());
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
