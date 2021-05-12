package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.dto.CaseTemplateConnDto;
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
        CaseTemplateConnDto dto = caseTemplateConnMapper.toCaseTemplateConnDto(conn);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateConnList method in the CaseTemplateConnMapper")
    void toCaseTemplateConnList_test() {
        List<CaseTemplateConnDto> dtoList = Lists.newArrayList(CaseTemplateConnDto.builder()
            .id(MOCK_ID).caseTemplateId(MOCK_ID).build());
        List<CaseTemplateConn> connList = caseTemplateConnMapper.toCaseTemplateConnList(dtoList);
        assertThat(connList.size()).isEqualTo(dtoList.size());
    }

}
