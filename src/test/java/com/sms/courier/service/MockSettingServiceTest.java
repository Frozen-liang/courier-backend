package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.mapper.MockSettingMapper;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.service.impl.MockSettingServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
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
    private final MockSettingMapper mockSettingMapper = mock(MockSettingMapper.class);
    private final MockSettingService mockSettingService = new MockSettingServiceImpl(mockSettingRepository,
        mockSettingMapper);

    private final static String MOCK_ID = new ObjectId().toString();

    @Test
    @DisplayName("Test the add method in the Mock Setting service")
    void editUrl_test() {
        Optional<MockSettingEntity> mockSettingEntity = Optional.ofNullable(MockSettingEntity.builder().build());
        when(mockSettingRepository.findById(any())).thenReturn(mockSettingEntity);
        when(mockSettingRepository.save(any())).thenReturn(MockSettingEntity.builder().build());
        Boolean isSuccess = mockSettingService.editUrl(MockSettingRequest.builder().id(MOCK_ID).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the Mock Setting service then exception")
    void editUrl_test_thenException() {
        when(mockSettingRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> mockSettingService.editUrl(MockSettingRequest.builder().id(MOCK_ID).build()))
            .isInstanceOf(
                ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the get method in the Mock Setting service")
    void get_test() {
        when(mockSettingRepository.findAll()).thenReturn(Lists.newArrayList(MockSettingEntity.builder().build()));
        when(mockSettingMapper.toResponse(any())).thenReturn(MockSettingResponse.builder().id(MOCK_ID).build());
        MockSettingResponse response = mockSettingService.get();
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the get method in the Mock Setting service return null")
    void get_test_ReturnNull() {
        when(mockSettingRepository.findAll()).thenReturn(null);
        when(mockSettingMapper.toResponse(any())).thenReturn(MockSettingResponse.builder().id(MOCK_ID).build());
        MockSettingResponse response = mockSettingService.get();
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Test the get method in the Mock Setting service then Exception")
    void get_test_thenException() {
        when(mockSettingRepository.findAll()).thenThrow(new RuntimeException());
        when(mockSettingMapper.toResponse(any())).thenReturn(MockSettingResponse.builder().id(MOCK_ID).build());
        assertThatThrownBy(mockSettingService::get).isInstanceOf(ApiTestPlatformException.class);
    }

}
