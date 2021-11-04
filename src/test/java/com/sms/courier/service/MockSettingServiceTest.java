package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.service.impl.MockSettingServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for MockSettingServiceTest")
public class MockSettingServiceTest {

    private final MockSettingRepository mockSettingRepository = mock(MockSettingRepository.class);
    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    private final MockSettingService mockSettingService = new MockSettingServiceImpl(mockSettingRepository,
        jwtTokenManager, accessTokenProperties);

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_TOKEN = "test";

    @Test
    @DisplayName("Test the add method in the Mock Setting service")
    void editUrl_test() {
        Optional<MockSettingEntity> mockSettingEntity = Optional.ofNullable(MockSettingEntity.builder().build());
        when(mockSettingRepository.findById(any())).thenReturn(mockSettingEntity);
        when(mockSettingRepository.save(any())).thenReturn(MockSettingEntity.builder().build());
        Boolean isSuccess = mockSettingService.edit(MockSettingRequest.builder().id(MOCK_ID).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the Mock Setting service then exception")
    void editUrl_test_thenException() {
        when(mockSettingRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> mockSettingService.edit(MockSettingRequest.builder().id(MOCK_ID).build()))
            .isInstanceOf(
                ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the get method in the Mock Setting service")
    void get_test() {
        when(mockSettingRepository.getFirstByOrderByModifyDateTimeDesc())
            .thenReturn(MockSettingResponse.builder().build());
        MockSettingResponse response = mockSettingService.findOne();
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the get method in the Mock Setting service then Exception")
    void get_test_thenException() {
        when(mockSettingRepository.getFirstByOrderByModifyDateTimeDesc()).thenThrow(new RuntimeException());
        assertThatThrownBy(mockSettingService::findOne).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the reset token method in the Mock Setting service")
    void resetToken_test() {
        Map<String, String> map = new HashMap<>();
        MockSettingEntity settingEntity = MockSettingEntity.builder().id(MOCK_ID).envVariable(map).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(settingEntity);
        when(mockSettingRepository.findById(any())).thenReturn(optional);
        when(jwtTokenManager.generateAccessToken(any())).thenReturn(MOCK_TOKEN);
        when(mockSettingRepository.save(any())).thenReturn(settingEntity);
        Boolean isSuccess = mockSettingService.resetToken(MOCK_ID);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the reset token method in the Mock Setting service")
    void resetToken_test_thenException() {
        when(mockSettingRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> mockSettingService.resetToken(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }
}
