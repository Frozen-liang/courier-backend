package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for CaseTemplateApiMapper")
public class CaseTemplateGroupMapperTest {

    private CaseTemplateGroupMapper caseTemplateGroupMapper = new CaseTemplateGroupMapperImpl();
    private static final String MOCK_ID = "1";
    private static final String NAME = "test";

    @Test
    @DisplayName("Test the toCaseTemplateGroupByAdd method in CaseTemplateGroupMapper")
    void toCaseTemplateGroupByAddTest() {
        CaseTemplateGroupRequest request = CaseTemplateGroupRequest.builder().build();
        assertThat(caseTemplateGroupMapper.toCaseTemplateGroupEntity(request)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateGroupByAdd method in CaseTemplateGroupMapper is null")
    void toCaseTemplateGroupByAddTest_isNull() {
        assertThat(caseTemplateGroupMapper.toCaseTemplateGroupEntity(null)).isNull();
    }

    @Test
    @DisplayName("Test the toResponseList method in CaseTemplateGroupMapper")
    void toResponseListTest() {
        List<CaseTemplateGroupEntity> caseTemplateGroupEntities = Lists.newArrayList();
        assertThat(caseTemplateGroupMapper.toResponse(caseTemplateGroupEntities)).isNotNull();
    }

    @Test
    @DisplayName("Test the toResponseList method in CaseTemplateGroupMapper is null")
    void toResponseListTest_isNull() {
        assertThat(caseTemplateGroupMapper.toResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the caseTemplateGroupEntityToCaseTemplateGroupResponse method in CaseTemplateGroupMapper")
    void caseTemplateGroupEntityToCaseTemplateGroupResponseTest() {
        CaseTemplateGroupEntity caseTemplateGroupEntity = CaseTemplateGroupEntity.builder().build();
        assertThat(caseTemplateGroupMapper.toResponse(Lists.newArrayList(caseTemplateGroupEntity))).isNotNull();
    }

    @Test
    @DisplayName("Test the caseTemplateGroupEntityToCaseTemplateGroupResponse method in CaseTemplateGroupMapper")
    void caseTemplateGroupEntityToCaseTemplateGroupResponse_isNull() {
        CaseTemplateGroupEntity dto = null;
        List<CaseTemplateGroupEntity> caseTemplateGroups = Lists.newArrayList(dto);
        assertThat(caseTemplateGroupMapper.toResponse(caseTemplateGroups)).size().isEqualTo(1);
    }

}
