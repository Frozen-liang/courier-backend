package com.sms.courier.mapper;

import com.google.common.collect.Lists;
import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for SceneCaseMapper")
class SceneCaseMapperTest {

    private SceneCaseMapper sceneCaseMapper = new SceneCaseMapperImpl();
    private static final String MOCK_ID = "1";
    private static final String NAME = "test";

    @Test
    @DisplayName("Test the toAddSceneCase method in the SceneCaseMapper")
    void toAddSceneCase_test() {
        AddSceneCaseRequest dto = AddSceneCaseRequest.builder()
                .tagId(Lists.newArrayList("tagId"))
                .groupId(MOCK_ID).name(NAME).build();
        SceneCaseEntity sceneCase = sceneCaseMapper.toAddSceneCase(dto);
        assertThat(sceneCase.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toUpdateSceneCase method in the SceneCaseMapper")
    void toUpdateSceneCase_test() {
        UpdateSceneCaseRequest dto = UpdateSceneCaseRequest.builder()
                .tagId(Lists.newArrayList("tafId"))
                .groupId(MOCK_ID).name(NAME).build();
        SceneCaseEntity sceneCase = sceneCaseMapper.toUpdateSceneCase(dto);
        assertThat(sceneCase.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toDto method in the SceneCaseMapper")
    void toDto_test() {
        SceneCaseEntity sceneCase = SceneCaseEntity.builder()
                .tagId(Lists.newArrayList("tagId"))
                .id(MOCK_ID).name(NAME).build();
        SceneCaseResponse dto = sceneCaseMapper.toDto(sceneCase);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toAddSceneCase_IsNull method in the SceneCaseMapper")
    void toAddSceneCase_IsNull_test() {
        SceneCaseEntity dto = sceneCaseMapper.toAddSceneCase(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the toUpdateSceneCase_IsNull method in the SceneCaseMapper")
    void toUpdateSceneCase_IsNull_test() {
        SceneCaseEntity dto  = sceneCaseMapper.toUpdateSceneCase(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the toDto_IsNull method in the SceneCaseMapper")
    void toDto_IsNull_test() {
        SceneCaseResponse dto  = sceneCaseMapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnList method in the SceneCaseMapper")
    void toCaseTemplateApiConnList_test() {
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        assertThat(sceneCaseMapper.toCaseTemplateApiConnList(caseTemplateApiList)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnList method in the SceneCaseMapper")
    void toCaseTemplateApiConnList_IsNull_test() {
        assertThat(sceneCaseMapper.toCaseTemplateApiConnList(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnList method in the SceneCaseMapper")
    void toCaseTemplateApiConn_IsNull_test() {
        assertThat(sceneCaseMapper.toCaseTemplateApiConn(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnListByResponse method in the SceneCaseMapper")
    void toCaseTemplateApiConnListByResponse_test() {
        List<CaseTemplateApiResponse> caseTemplateApiResponseList = Lists.newArrayList(CaseTemplateApiResponse
                .builder()
                .build());
        assertThat(sceneCaseMapper.toCaseTemplateApiConnListByResponse(caseTemplateApiResponseList)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnByResponse method in the SceneCaseMapper")
    void toCaseTemplateApiConnByResponse_IsNull_test() {
        assertThat(sceneCaseMapper.toCaseTemplateApiConnByResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnListByResponse method in the SceneCaseMapper")
    void toCaseTemplateApiConnListByResponsen_IsNull_test() {
        assertThat(sceneCaseMapper.toCaseTemplateApiConnListByResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnListByResponse method in the SceneCaseMapper")
    void caseTemplateApiApiTestCaseExecute_IsNull_test() {
        CaseTemplateApiEntity caseTemplateApi = CaseTemplateApiEntity.builder()
                .apiTestCase(ApiTestCaseEntity.builder().execute(Boolean.FALSE).build())
                .build();
        assertThat(sceneCaseMapper.toCaseTemplateApiConn(caseTemplateApi)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiConnListByResponse method in the SceneCaseMapper")
    void toCaseTemplateApiConnByResponse_test() {
        CaseTemplateApiResponse caseTemplateApi = CaseTemplateApiResponse.builder()
                .apiTestCase(ApiTestCaseResponse.builder().execute(Boolean.FALSE).build())
                .build();
        assertThat(sceneCaseMapper.toCaseTemplateApiConnByResponse(caseTemplateApi)).isNotNull();
    }

}
