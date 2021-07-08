package com.sms.satp.repository;

import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.repository.impl.CustomizedCaseTemplateRepositoryImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for CustomizedCaseTemplateRepositoryTest")
class CustomizedCaseTemplateRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository =
       new CustomizedCaseTemplateRepositoryImpl(mongoTemplate);

    private final static String MOCK_ID = "1";
    private final static String NAME = "test";
    private final static Long COUNT = 1L;

    @Test
    @DisplayName("Test the search method in the CustomizedCaseTemplateRepository")
    void search_test() {
        List<CaseTemplate> caseTemplateList = Lists.newArrayList(CaseTemplate.builder().id(MOCK_ID).build());
        when(mongoTemplate.find(any(Query.class), eq(CaseTemplate.class))).thenReturn(caseTemplateList);
        when(mongoTemplate.count(any(Query.class), eq(CaseTemplate.class))).thenReturn(COUNT);
        CaseTemplateSearchRequest request = new CaseTemplateSearchRequest();
        request.setTagId(Lists.newArrayList(new ObjectId()));
        request.setGroupId(new ObjectId());
        request.setName(NAME);
        request.setCreateUserName(Lists.newArrayList(NAME));
        request.setTestStatus(Lists.newArrayList(MOCK_ID));
        request.setRemoved(Boolean.FALSE);
        request.setPageNumber(1);
        request.setPageSize(1);
        Page<CaseTemplateResponse> page = customizedCaseTemplateRepository.page(request, new ObjectId());
        assertThat(page).isNotNull();
    }

}
