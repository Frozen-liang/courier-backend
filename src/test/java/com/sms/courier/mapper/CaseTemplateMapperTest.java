package com.sms.courier.mapper;

import com.google.common.collect.Lists;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for CaseTemplateMapper")
class CaseTemplateMapperTest {

    private CaseTemplateMapper caseTemplateMapper = new CaseTemplateMapperImpl();
    private static final String MOCK_ID = "1";
    private static final String NAME = "test";

    @Test
    @DisplayName("Test the toCaseTemplate method in the CaseTemplateMapper")
    void toCaseTemplate_test() {
        CaseTemplateResponse dto = CaseTemplateResponse.builder()
                .tagId(Lists.newArrayList()).id(MOCK_ID).groupId(MOCK_ID).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplate(dto);
        assertThat(caseTemplate.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplate method in the CaseTemplateMapper")
    void toCaseTemplate_isNull_test(){
        assertThat(caseTemplateMapper.toCaseTemplate(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateByUpdateRequest method in the CaseTemplateMapper")
    void toCaseTemplateByUpdateRequest_test() {
        UpdateCaseTemplateRequest request = UpdateCaseTemplateRequest.builder().id(MOCK_ID).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateByUpdateRequest(request);
        assertThat(caseTemplate.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateByUpdateRequest method in the CaseTemplateMapper")
    void toCaseTemplateByUpdateRequest_isNull_test() {
        assertThat(caseTemplateMapper.toCaseTemplateByUpdateRequest(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateByAddRequest method in the CaseTemplateMapper")
    void toCaseTemplateByAddRequest_test() {
        AddCaseTemplateRequest addCaseTemplateRequest = AddCaseTemplateRequest.builder()
                .tagId(Lists.newArrayList()).name(NAME).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateByAddRequest(addCaseTemplateRequest);
        assertThat(caseTemplate.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toCaseTemplateByAddRequest method in the CaseTemplateMapper")
    void toCaseTemplateByAddRequest_isNull_test(){
        assertThat(caseTemplateMapper.toCaseTemplateByAddRequest(null)).isNull();
    }

    @Test
    @DisplayName("Test the toDto method in the CaseTemplateMapper")
    void toDto_test() {
        CaseTemplateEntity caseTemplate = CaseTemplateEntity.builder()
                .tagId(Lists.newArrayList()).id(MOCK_ID).groupId(MOCK_ID).build();
        CaseTemplateResponse dto = caseTemplateMapper.toDto(caseTemplate);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toDto method in the CaseTemplateMapper")
    void toDto_isNull_test() {
        assertThat(caseTemplateMapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateBySceneCase method in the CaseTemplateMapper")
    void toCaseTemplateBySceneCase_Test(){
        SceneCaseEntity sceneCase=SceneCaseEntity.builder().tagId(Lists.newArrayList()).build();
        assertThat(caseTemplateMapper.toCaseTemplateBySceneCase(sceneCase)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateBySceneCase method in the CaseTemplateMapper")
    void toCaseTemplateBySceneCase_isNull_Test(){
        assertThat(caseTemplateMapper.toCaseTemplateBySceneCase(null)).isNull();
    }

}
