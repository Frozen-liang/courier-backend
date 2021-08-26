package com.sms.courier.repository;

import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.response.MockApiResponse;
import com.sms.courier.repository.impl.CustomizedMockApiRepositoryImpl;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedMockApiRepositoryTest")
public class CustomizedMockApiRepositoryTest {

    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedMockApiRepository customizedMockApiRepository =
        new CustomizedMockApiRepositoryImpl(commonRepository);

    private final static ObjectId MOCK_OBJECT_ID = new ObjectId();

    @Test
    @DisplayName("Test the page method in the MockApi service")
    void page_test() {
        Page<MockApiResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(MockApiResponse.builder().build()));
        when(commonRepository.page(any(),any(),eq(MockApiResponse.class))).thenReturn(page);
        Page<MockApiResponse> responses = customizedMockApiRepository.page(MOCK_OBJECT_ID, MockApiPageRequest.builder()
            .isEnable(Boolean.TRUE).build());
        assertThat(responses.getContent()).isNotEmpty();
    }

}
