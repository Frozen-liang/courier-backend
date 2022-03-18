package com.sms.courier.repository;

import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.repository.impl.CustomizedSceneCaseJobRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedSceneCaseJobRepositoryTest")
class CustomizedSceneCaseJobRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        new CustomizedSceneCaseJobRepositoryImpl(mongoTemplate);

    private final static String MOCK_ID = "1";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the getGroupDayCount method in the CommonStatisticsRepositoryTest")
    public void getGroupDayCount_test() {
        AggregationResults<CountStatisticsResponse> results = mock(AggregationResults.class);
        List<CountStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CountStatisticsResponse.builder().day(LocalDate.now()).count(0).build());
        when(results.getMappedResults()).thenReturn(caseCountStatisticsResponses);
        when(mongoTemplate.aggregate(any(), eq(SceneCaseJobEntity.class), eq(CountStatisticsResponse.class)))
            .thenReturn(results);
        List<CountStatisticsResponse> responses =
            customizedSceneCaseJobRepository.getGroupDayCount(Lists.newArrayList(MOCK_ID), LocalDateTime.now());
        assertThat(responses).isNotEmpty();
    }
}
