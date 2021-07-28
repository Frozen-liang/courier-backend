package com.sms.satp.repository;

import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.repository.impl.CustomizedCaseTemplateRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
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
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Tests for CustomizedCaseTemplateRepositoryTest")
class CustomizedCaseTemplateRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository =
        new CustomizedCaseTemplateRepositoryImpl(mongoTemplate, commonRepository);

    private final static String MOCK_ID = "1";
    private final static String NAME = "test";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the search method in the CustomizedCaseTemplateRepository")
    void search_test() {
        ArrayList<CaseTemplateResponse> apiDtoList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            apiDtoList.add(new CaseTemplateResponse());
        }
        CaseTemplateSearchRequest request = new CaseTemplateSearchRequest();
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
        Page<CaseTemplateResponse> page = customizedCaseTemplateRepository.page(request, new ObjectId());
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedCaseTemplateRepository")
    void deleteByIds_test() {
        when(commonRepository.deleteByIds(any(),any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedCaseTemplateRepository.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedCaseTemplateRepository")
    void recover_test() {
        when(commonRepository.recover(any(),any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedCaseTemplateRepository.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the getCaseTemplateIdsByGroupIds method in the CustomizedCaseTemplateRepository")
    void getCaseTemplateIdsByGroupIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateEntity.builder().build()));
        List<CaseTemplateEntity> dto =
            customizedCaseTemplateRepository.getCaseTemplateIdsByGroupIds(Lists.newArrayList(MOCK_ID));
        assertThat(dto).isNotEmpty();
    }
}
