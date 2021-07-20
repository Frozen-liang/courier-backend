package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.impl.CustomizedApiRepositoryImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

@DisplayName("Tests for CustomizedApiRepositoryTest")
class CustomizedApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(CommonDeleteRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final CustomizedApiRepository customizedApiRepository = new CustomizedApiRepositoryImpl(mongoTemplate,
        commonDeleteRepository, apiGroupRepository);
    private static final Long TOTAL_ELEMENTS = 20L;
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the page method in the CustomizedApiRepository")
    public void page_test() {
        ArrayList<ApiResponse> apiDtoList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiDtoList.add(new ApiResponse());
        }
        ApiPageRequest apiPageRequest = new ApiPageRequest();
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setApiStatus(Arrays.asList(1, 2));
        apiPageRequest.setProjectId(new ObjectId());
        apiPageRequest.setRequestMethod(Arrays.asList(1, 2));
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setTagId(Arrays.asList(new ObjectId(), new ObjectId()));
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(TOTAL_ELEMENTS);
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(mongoTemplate.aggregate(any(), any(Class.class), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getMappedResults()).thenReturn(apiDtoList);
        Page<ApiResponse> page = customizedApiRepository.page(apiPageRequest);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("Test the deleteById method in the CustomizedApiRepository")
    public void deleteById_test() {
        when(commonDeleteRepository.deleteById(ID, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedApiRepository")
    public void deleteByIds() {
        when(commonDeleteRepository.deleteByIds(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteByIds(ID_LIST)).isTrue();
    }
}
