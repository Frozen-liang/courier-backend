package com.sms.courier.service;

import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.mapper.MockApiMapper;
import com.sms.courier.repository.MockApiRepository;
import com.sms.courier.service.impl.MockApiServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for MockApiServiceTest")
public class MockApiServiceTest {

    private final MockApiRepository mockApiRepository = mock(MockApiRepository.class);
    private final MockApiMapper mockApiMapper = mock(MockApiMapper.class);
    private final MockApiService mockApiService = new MockApiServiceImpl(mockApiRepository, mockApiMapper);

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

}
