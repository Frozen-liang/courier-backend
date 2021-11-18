package com.sms.courier.repository;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
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
        AggregationResults<CaseCountStatisticsResponse> results = mock(AggregationResults.class);
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountStatisticsResponse.builder().day(LocalDate.now()).count(0).build());
        when(results.getMappedResults()).thenReturn(caseCountStatisticsResponses);
        when(mongoTemplate.aggregate(any(), eq(ApiTestCaseEntity.class), eq(CaseCountStatisticsResponse.class)))
            .thenReturn(results);
        List<CaseCountStatisticsResponse> responses =
            commonStatisticsRepository.getGroupDayCount(Lists.newArrayList(ID), LocalDateTime.now(),
                ApiTestCaseEntity.class);
        assertThat(responses).isNotEmpty();
    }

}
