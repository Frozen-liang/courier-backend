package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.repository.impl.CustomizedCaseTemplateRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedCaseTemplateRepositoryTest")
class CustomizedCaseTemplateRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final CaseTemplateGroupRepository caseTemplateGroupRepository = mock(CaseTemplateGroupRepository.class);
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository =
        new CustomizedCaseTemplateRepositoryImpl(mongoTemplate, commonRepository, caseTemplateGroupRepository);

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
        request.setName(NAME);
        request.setCreateUserId(Lists.newArrayList(new ObjectId()));
        request.setTestStatus(Lists.newArrayList(MOCK_ID));
        request.setRemoved(Boolean.FALSE);
        request.setPageNumber(1);
        request.setPageSize(1);
        Page<CaseTemplateResponse> responses = mock(Page.class);
        when(responses.getContent()).thenReturn(apiDtoList);
        when(commonRepository.page(any(QueryVo.class),any(),eq(CaseTemplateResponse.class))).thenReturn(responses);
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

    @Test
    @DisplayName("Test the deleteGroupIdByIds method in the CustomizedCaseTemplateRepository")
    void deleteGroupIdByIds_test() {
        when(commonRepository.deleteFieldByIds(any(),any(),any())).thenReturn(Boolean.TRUE);
        assertTrue(customizedCaseTemplateRepository.deleteGroupIdByIds(Lists.newArrayList(MOCK_ID)));
    }

    @Test
    @DisplayName("Test the findById method in the CustomizedCaseTemplateRepository")
    void findById_test() {
        Optional<CaseTemplateResponse> optional = Optional.ofNullable(CaseTemplateResponse.builder().build());
        when(commonRepository.findById(any(), any(), any(List.class), eq(CaseTemplateResponse.class)))
            .thenReturn(optional);
        Optional<CaseTemplateResponse> response = customizedCaseTemplateRepository.findById(MOCK_ID);
        assertThat(response).isNotEmpty();
    }
}
