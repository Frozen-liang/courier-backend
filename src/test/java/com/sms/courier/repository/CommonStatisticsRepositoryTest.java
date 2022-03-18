package com.sms.courier.repository;

import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.repository.impl.CommonStatisticsRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CommonStatisticsRepositoryTest")
public class CommonStatisticsRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonStatisticsRepository commonStatisticsRepository =
        new CommonStatisticsRepositoryImpl(mongoTemplate);

    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the getGroupDayCount method in the CommonStatisticsRepositoryTest")
    public void getGroupDayCount_test() {
        AggregationResults<CountStatisticsResponse> results = mock(AggregationResults.class);
        List<CountStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CountStatisticsResponse.builder().day(LocalDate.now()).count(0).build());
        when(results.getMappedResults()).thenReturn(caseCountStatisticsResponses);
        when(mongoTemplate.aggregate(any(), eq(ApiTestCaseEntity.class), eq(CountStatisticsResponse.class)))
            .thenReturn(results);
        List<CountStatisticsResponse> responses =
            commonStatisticsRepository.getGroupDayCount(Lists.newArrayList(ID), LocalDateTime.now(),
                ApiTestCaseEntity.class);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getGroupUserCount method in the CommonStatisticsRepositoryTest")
    public void getGroupUserCount_test() {
        AggregationResults<CaseCountUserStatisticsResponse> results = mock(AggregationResults.class);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().count(0).build());
        when(results.getMappedResults()).thenReturn(caseCountStatisticsResponses);
        when(mongoTemplate.aggregate(any(), eq(ApiTestCaseEntity.class), eq(CaseCountUserStatisticsResponse.class)))
            .thenReturn(results);
        List<CaseCountUserStatisticsResponse> responses =
            commonStatisticsRepository.getGroupUserCount(Lists.newArrayList(ID), LocalDateTime.now(),
                ApiTestCaseEntity.class);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getGroupUserCountByJob method in the CommonStatisticsRepositoryTest")
    public void getGroupUserCountByJob_test() {
        AggregationResults<CaseCountUserStatisticsResponse> results = mock(AggregationResults.class);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().count(0).build());
        when(results.getMappedResults()).thenReturn(caseCountStatisticsResponses);
        when(mongoTemplate.aggregate(any(), eq(ApiTestCaseEntity.class), eq(CaseCountUserStatisticsResponse.class)))
            .thenReturn(results);
        List<CaseCountUserStatisticsResponse> responses =
            commonStatisticsRepository.getGroupUserCountByJob(Lists.newArrayList(ID), LocalDateTime.now(),
                ApiTestCaseEntity.class);
        assertThat(responses).isNotEmpty();
    }
}
