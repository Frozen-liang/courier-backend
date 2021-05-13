package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.dto.response.CaseTemplateConnResponse;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for CaseTemplateConnMapper")
class CaseTemplateConnMapperTest {

    private CaseTemplateConnMapper caseTemplateConnMapper = new CaseTemplateConnMapperImpl();
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toCaseTemplateConnDto method in the CaseTemplateConnMapper")
    void toCaseTemplateConnDto_test() {
        CaseTemplateConn conn = CaseTemplateConn.builder()
            .id(MOCK_ID)
            .sceneCaseId(MOCK_ID)
            .build();
        CaseTemplateConnResponse dto = caseTemplateConnMapper.toCaseTemplateConnDto(conn);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateConnList method in the CaseTemplateConnMapper")
    void toCaseTemplateConnList_test() {
        List<CaseTemplateConnResponse> dtoList = Lists.newArrayList(CaseTemplateConnResponse.builder()
            .id(MOCK_ID).caseTemplateId(MOCK_ID).build());
        List<CaseTemplateConn> connList = caseTemplateConnMapper.toCaseTemplateConnList(dtoList);
        assertThat(connList.size()).isEqualTo(dtoList.size());
    }

    @Test
    @DisplayName("Test the toCaseTemplateConn method in the CaseTemplateConnMapper")
    void toCaseTemplateConn_test() {
        AddCaseTemplateConnRequest addCaseTemplateConnRequest =
            AddCaseTemplateConnRequest.builder().caseTemplateId(MOCK_ID).build();
        CaseTemplateConn caseTemplateConn = caseTemplateConnMapper.toCaseTemplateConn(addCaseTemplateConnRequest);
        assertThat(caseTemplateConn.getCaseTemplateId()).isEqualTo(MOCK_ID);
    }
}
