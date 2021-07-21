package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
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
        CaseTemplateResponse dto = CaseTemplateResponse.builder().id(MOCK_ID).groupId(MOCK_ID).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplate(dto);
        assertThat(caseTemplate.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateByUpdateRequest method in the CaseTemplateMapper")
    void toCaseTemplateByUpdateRequest_test() {
        UpdateCaseTemplateRequest request = UpdateCaseTemplateRequest.builder().id(MOCK_ID).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateByUpdateRequest(request);
        assertThat(caseTemplate.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateByAddRequest method in the CaseTemplateMapper")
    void toCaseTemplateByAddRequest_test() {
        AddCaseTemplateRequest addCaseTemplateRequest = AddCaseTemplateRequest.builder().name(NAME).build();
        CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateByAddRequest(addCaseTemplateRequest);
        assertThat(caseTemplate.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toDto method in the CaseTemplateMapper")
    void toDto_test() {
        CaseTemplateEntity caseTemplate = CaseTemplateEntity.builder().id(MOCK_ID).groupId(MOCK_ID).build();
        CaseTemplateResponse dto = caseTemplateMapper.toDto(caseTemplate);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

}
