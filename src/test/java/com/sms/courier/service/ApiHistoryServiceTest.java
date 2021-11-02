package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.response.ApiHistoryDetailResponse;
import com.sms.courier.dto.response.ApiHistoryListResponse;
import com.sms.courier.dto.response.ApiHistoryResponse;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.impl.ApiHistoryServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for ApiHistoryService")
public class ApiHistoryServiceTest {

    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiHistoryService apiHistoryService = new ApiHistoryServiceImpl(commonRepository);

    @DisplayName("Test findByApiId method in ApiHistoryService")
    @Test
    public void findByApiId_test() {
        when(commonRepository.listLookupUser(any(), any(), any(Class.class))).thenReturn(Collections.emptyList());
        List<ApiHistoryListResponse> result = apiHistoryService.findByApiId(new ObjectId());
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findById method in ApiHistoryService")
    @Test
    public void findById_test() {
        ApiHistoryResponse apiHistoryResponse = new ApiHistoryResponse();
        apiHistoryResponse.setRecord(new ApiHistoryDetailResponse());
        when(commonRepository.findById(any(), any(), any(List.class), any(Class.class))).thenReturn(Optional.of(apiHistoryResponse));
        ApiHistoryDetailResponse result = apiHistoryService.findById("ID");
        assertThat(result).isNotNull();
    }
}
