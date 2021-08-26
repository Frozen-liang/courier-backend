package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.MockApiResponse;
import com.sms.courier.dto.response.PageMockApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.mapper.ApiMapper;
import com.sms.courier.mapper.MockApiMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedMockApiRepository;
import com.sms.courier.repository.MockApiRepository;
import com.sms.courier.service.impl.MockApiServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for MockApiServiceTest")
public class MockApiServiceTest {

    private final MockApiRepository mockApiRepository = mock(MockApiRepository.class);
    private final MockApiMapper mockApiMapper = mock(MockApiMapper.class);
    private final CustomizedMockApiRepository customizedMockApiRepository = mock(CustomizedMockApiRepository.class);
    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiMapper apiMapper = mock(ApiMapper.class);
    private final MockApiService mockApiService = new MockApiServiceImpl(mockApiRepository, mockApiMapper,
        customizedMockApiRepository, apiRepository, apiMapper);

    private final static ObjectId MOCK_OBJECT_ID = new ObjectId();
    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";

    @Test
    @DisplayName("Test the add method in the MockApi service")
    void add_test() {
        MockApiEntity mockApiEntity = MockApiEntity.builder().id(MOCK_ID).mockName(MOCK_NAME).build();
        when(mockApiMapper.toEntity(any())).thenReturn(mockApiEntity);
        Boolean isSuccess = mockApiService.add(MockApiRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the MockApi service then Exception")
    void add_test_thenException() {
        when(mockApiMapper.toEntity(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> mockApiService.add(MockApiRequest.builder().build()));
    }

    @Test
    @DisplayName("Test the page method in the MockApi service")
    void page_test() {
        ApiEntity apiEntity = ApiEntity.builder().build();
        when(apiRepository.findApiEntityByIdAndRemoved(any(),anyBoolean())).thenReturn(apiEntity);
        Page<MockApiResponse> mockApiResponsePage = mock(Page.class);
        when(customizedMockApiRepository.page(any(),any())).thenReturn(mockApiResponsePage);
        when(apiMapper.toDto(any())).thenReturn(ApiResponse.builder().build());
        PageMockApiResponse response = mockApiService.page(MOCK_OBJECT_ID, MockApiPageRequest.builder().build());
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the MockApi service return null")
    void page_apiEntityIsNull_test() {
        when(apiRepository.findApiEntityByIdAndRemoved(any(),anyBoolean())).thenReturn(null);
        PageMockApiResponse response = mockApiService.page(MOCK_OBJECT_ID, MockApiPageRequest.builder().build());
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Test the page method in the MockApi service then Exception")
    void page_test_thenException() {
        when(apiRepository.findApiEntityByIdAndRemoved(any(),anyBoolean())).thenThrow(new RuntimeException());
        assertThatThrownBy(()->mockApiService.page(MOCK_OBJECT_ID, MockApiPageRequest.builder().build())).isInstanceOf(
            ApiTestPlatformException.class);
    }

}
