package com.sms.satp.repository;

import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.repository.impl.CustomizedSceneCaseRepositoryImpl;
import java.util.ArrayList;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedSceneCaseRepositoryTest")
class CustomizedSceneCaseRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(CommonDeleteRepository.class);
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository =
        new CustomizedSceneCaseRepositoryImpl(mongoTemplate, commonDeleteRepository);

    private final static String MOCK_ID = "1";
    private final static String NAME = "test";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the search method in the CustomizedSceneCaseRepository")
    void search_test() {
        ArrayList<SceneCaseResponse> apiDtoList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            apiDtoList.add(new SceneCaseResponse());
        }
        SearchSceneCaseRequest request = new SearchSceneCaseRequest();
        request.setTagId(Lists.newArrayList(new ObjectId()));
        request.setGroupId(new ObjectId());
        request.setName(NAME);
        request.setCreateUserName(Lists.newArrayList(NAME));
        request.setTestStatus(Lists.newArrayList(MOCK_ID));
        request.setRemoved(Boolean.FALSE);
        request.setPageNumber(1);
        request.setPageSize(1);
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(COUNT);
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(mongoTemplate.aggregate(any(), any(Class.class), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getMappedResults()).thenReturn(apiDtoList);
        Page<SceneCaseResponse> page = customizedSceneCaseRepository.search(request, new ObjectId());
        assertThat(page).isNotNull();
    }

}
