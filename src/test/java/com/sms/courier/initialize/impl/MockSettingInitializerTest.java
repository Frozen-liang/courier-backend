package com.sms.courier.initialize.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.jwt.JwtTokenManager;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for MockSettingInitializerTest")
public class MockSettingInitializerTest {

    private final ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final MockSettingRepository mockSettingRepository = mock(MockSettingRepository.class);
    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final MockSettingInitializer mockSettingInitializer = new MockSettingInitializer();

    private static final String ID = ObjectId.get().toString();
    private final MockSettingEntity entity = MockSettingEntity.builder().id(ID).build();

    @Test
    void init_test() {
        when(applicationContext.getBean(MockSettingRepository.class)).thenReturn(mockSettingRepository);
        when(applicationContext.getBean(JwtTokenManager.class)).thenReturn(jwtTokenManager);
        when(mockSettingRepository.findFirstByOrderByCreateDateTimeDesc()).thenReturn(Optional.empty());
        when(applicationContext.getBean(AccessTokenProperties.class)).thenReturn(new AccessTokenProperties());
        when(jwtTokenManager.generateAccessToken(any())).thenReturn("test");
        when(mockSettingRepository.insert(any(MockSettingEntity.class)))
            .thenReturn(entity);
        mockSettingInitializer.init(applicationContext);
        verify(mockSettingRepository, times(1)).save(any());
    }

}
