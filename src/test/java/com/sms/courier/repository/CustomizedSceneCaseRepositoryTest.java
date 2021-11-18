package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.impl.CustomizedSceneCaseRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@DisplayName("Tests for CustomizedSceneCaseRepositoryTest")
class CustomizedSceneCaseRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final SceneCaseGroupRepository sceneCaseGroupRepository = mock(SceneCaseGroupRepository.class);
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository =
        new CustomizedSceneCaseRepositoryImpl(mongoTemplate, commonRepository, sceneCaseGroupRepository);

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
        request.setName(NAME);
        request.setCreateUserName(Lists.newArrayList(NAME));
        request.setTestStatus(Lists.newArrayList(MOCK_ID));
        request.setRemoved(Boolean.FALSE);
        request.setPageNumber(1);
        request.setPageSize(1);
        Page<SceneCaseResponse> responses = mock(Page.class);
        when(responses.getContent()).thenReturn(apiDtoList);
        when(commonRepository.page(any(QueryVo.class), any(), eq(SceneCaseResponse.class))).thenReturn(responses);
        Page<SceneCaseResponse> page = customizedSceneCaseRepository.search(request, new ObjectId());
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedSceneCaseRepository")
    void deleteByIds_test() {
        when(commonRepository.deleteByIds(any(), any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedSceneCaseRepository.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedSceneCaseRepository")
    void recover_test() {
        when(commonRepository.recover(any(), any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = customizedSceneCaseRepository.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the getSceneCaseIdsByGroupIds method in the CustomizedSceneCaseRepository")
    void getSceneCaseIdsByGroupIds_test() {
        when(mongoTemplate.find(any(), any()))
            .thenReturn(Lists.newArrayList(CaseTemplateEntity.builder().build()));
        List<SceneCaseEntity> dto =
            customizedSceneCaseRepository.getSceneCaseIdsByGroupIds(Lists.newArrayList(MOCK_ID));
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the deleteGroupIdByIds method in the CustomizedSceneCaseRepository")
    void deleteGroupIdByIds_test() {
        when(commonRepository.deleteFieldByIds(any(), any(), any())).thenReturn(Boolean.TRUE);
        assertTrue(customizedSceneCaseRepository.deleteGroupIdByIds(Lists.newArrayList(MOCK_ID)));
    }

    @Test
    @DisplayName("Test the findById method in the CustomizedSceneCaseRepository")
    void findById_test() {
        Optional<SceneCaseResponse> optional = Optional.ofNullable(SceneCaseResponse.builder().build());
        when(commonRepository.findById(any(), any(), any(List.class), eq(SceneCaseResponse.class)))
            .thenReturn(optional);
        Optional<SceneCaseResponse> response = customizedSceneCaseRepository.findById(MOCK_ID);
        assertThat(response).isNotEmpty();
    }

}
