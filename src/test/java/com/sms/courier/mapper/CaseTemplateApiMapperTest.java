package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.dto.request.AddCaseTemplateApiRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for CaseTemplateApiMapper")
class CaseTemplateApiMapperTest {

    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final ParamInfoMapper paramInfoMapper = mock(ParamInfoMapper.class);
    private final ResponseHeadersVerificationMapper responseHeadersVerificationMapper = mock(
        ResponseHeadersVerificationMapper.class);
    private final ResponseResultVerificationMapper resultVerificationMapper = mock(
        ResponseResultVerificationMapper.class);
    private final MatchParamInfoMapper matchParamInfoMapper = mock(MatchParamInfoMapper.class);

    private final CaseTemplateApiMapper caseTemplateApiMapper = new CaseTemplateApiMapperImpl(apiTestCaseMapper,
        paramInfoMapper,matchParamInfoMapper, responseHeadersVerificationMapper, resultVerificationMapper);
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toCaseTemplateApiByUpdateRequest method in the CaseTemplateApiMapper")
    void toCaseTemplateApiByUpdateRequest_test() {
        UpdateCaseTemplateApiRequest request = getTemplateDto();
        CaseTemplateApiEntity caseTemplateApi = caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(request);
        assertThat(caseTemplateApi.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiDto method in the CaseTemplateApiMapper")
    void toCaseTemplateApiDto_test() {
        CaseTemplateApiEntity api = getTemplateApi();
        CaseTemplateApiResponse dto = caseTemplateApiMapper.toCaseTemplateApiDto(api);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListByUpdateRequestList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListByUpdateRequestList_test() {
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList = Lists.newArrayList(getTemplateDto());
        List<CaseTemplateApiEntity> apiList = caseTemplateApiMapper
            .toCaseTemplateApiListByUpdateRequestList(updateCaseTemplateApiRequestList);
        assertThat(apiList.size()).isEqualTo(updateCaseTemplateApiRequestList.size());
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListByAddRequestList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListByAddRequestList_test() {
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList = Lists.newArrayList(
            AddCaseTemplateApiRequest.builder().build());
        List<CaseTemplateApiEntity> caseTemplateApiList =
            caseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(addCaseTemplateApiRequestList);
        assertThat(caseTemplateApiList).isNotEmpty();
    }

    private CaseTemplateApiEntity getTemplateApi() {
        return CaseTemplateApiEntity.builder()
            .id(MOCK_ID)
            .apiType(ApiType.API)
            .apiTestCase(ApiTestCaseEntity.builder()
                .apiEntity(ApiEntity.builder()
                    .apiProtocol(ApiProtocol.HTTPS)
                    .requestMethod(RequestMethod.GET)
                    .apiRequestParamType(ApiRequestParamType.FORM_DATA)
                    .apiResponseJsonType(ApiJsonType.OBJECT)
                    .apiRequestJsonType(ApiJsonType.OBJECT)
                    .id(MOCK_ID).build())
                .build())
            .caseTemplateId(MOCK_ID)
            .build();
    }

    private UpdateCaseTemplateApiRequest getTemplateDto() {
        return UpdateCaseTemplateApiRequest.builder()
            .id(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .build();
    }

    private CaseTemplateApiResponse getTemplateApiResponse() {
        return CaseTemplateApiResponse.builder()
            .id(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .build();
    }


    @Test
    @DisplayName("Test the updateCaseTemplateApiRequest in toCaseTemplateApiByUpdateRequest is null")
    void toCaseTemplateApiByUpdateRequest_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(null)).isNull();
    }

    @Test
    @DisplayName("Test the caseTemplateApi in toCaseTemplateApiDto is null")
    void toCaseTemplateApiDto_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiDto(null)).isNull();
    }

    @Test
    @DisplayName("Test the updateCaseTemplateApiRequestList in toCaseTemplateApiListByUpdateRequestList is null")
    void toCaseTemplateApiListByUpdateRequestList_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(null)).isNull();
    }

    @Test
    @DisplayName("Test the addCaseTemplateApiRequestList in toCaseTemplateApiListByAddRequestList is null")
    void toCaseTemplateApiListByAddRequestList_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(null)).isNull();
    }

    @Test
    @DisplayName("Test the addCaseTemplateApiRequest in toCaseTemplateApi is null")
    void toCaseTemplateApi_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApi(null)).isNull();
    }

    @Test
    @DisplayName("Test the toSceneCaseList method in the CaseTemplateApiMapper")
    void toSceneCaseListTest() {
        List<CaseTemplateApiEntity> caseTemplateApiEntities = Lists.newArrayList();
        assertThat(caseTemplateApiMapper.toSceneCaseList(caseTemplateApiEntities)).isNotNull();
    }

    @Test
    @DisplayName("Test the toSceneCaseList is Null method in the CaseTemplateApiMapper")
    void toSceneCaseListTest_isNull() {
        assertThat(caseTemplateApiMapper.toSceneCaseList(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListBySceneCaseApiList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListBySceneCaseApiListTest() {
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList();
        assertThat(caseTemplateApiMapper.toCaseTemplateApiListBySceneCaseApiList(sceneCaseApiList)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListBySceneCaseApiList is null method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListBySceneCaseApiListTest_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiListBySceneCaseApiList(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiBySceneCaseApi method in the CaseTemplateApiMapper")
    void toCaseTemplateApiBySceneCaseApiTest() {
        SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder().build();
        assertThat(caseTemplateApiMapper.toCaseTemplateApiBySceneCaseApi(sceneCaseApi)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiBySceneCaseApi method in the CaseTemplateApiMapper")
    void toCaseTemplateApiBySceneCaseApiTest_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiBySceneCaseApi(null)).isNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiDtoList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiDtoListTest() {
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList();
        assertThat(caseTemplateApiMapper.toCaseTemplateApiDtoList(caseTemplateApiList)).isNotNull();
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiDtoList is null method in the CaseTemplateApiMapper")
    void toCaseTemplateApiDtoListTest_isNull() {
        assertThat(caseTemplateApiMapper.toCaseTemplateApiDtoList(null)).isNull();
    }

    @Test
    @DisplayName("Test the apiTestCaseRequestToApiTestCaseEntity is null method in the CaseTemplateApiMapper")
    void apiTestCaseRequestToApiTestCaseEntityTest() {
        ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
            .tagId(Lists.newArrayList())
            .apiEntity(ApiRequest.builder()
                .requestHeaders(Lists.newArrayList())
                .responseHeaders(Lists.newArrayList())
                .pathParams(Lists.newArrayList())
                .restfulParams(Lists.newArrayList())
                .responseParams(Lists.newArrayList())
                .responseParams(Lists.newArrayList())
                .requestParams(Lists.newArrayList())
                .build())
            .build();
        AddCaseTemplateApiRequest request = AddCaseTemplateApiRequest.builder().apiTestCase(apiTestCaseRequest).build();
        assertThat(caseTemplateApiMapper.toCaseTemplateApi(request)).isNotNull();
    }

    @Test
    @DisplayName("Test the apiTestCaseRequestToApiTestCaseEntity is null method in the CaseTemplateApiMapper")
    void toSceneCaseList_Test() {
        CaseTemplateApiEntity caseTemplateApiEntity = CaseTemplateApiEntity.builder().build();
        assertThat(caseTemplateApiMapper.toSceneCaseList(Lists.newArrayList(caseTemplateApiEntity))).isNotNull();
    }

    @Test
    @DisplayName("Test the caseTemplateApiEntityToSceneCaseApiEntity method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListBySceneCaseApiList_Test() {
        SceneCaseApiEntity sceneCaseApiEntity = SceneCaseApiEntity.builder().build();
        assertThat(
            caseTemplateApiMapper.toCaseTemplateApiListBySceneCaseApiList(Lists.newArrayList(sceneCaseApiEntity)))
            .isNotNull();
    }

    @Test
    @DisplayName("Test the caseTemplateApiEntityToSceneCaseApiEntity method in the CaseTemplateApiMapper")
    void toCaseTemplateApiDtoList_Test() {
        CaseTemplateApiEntity caseTemplateApiEntity = CaseTemplateApiEntity.builder().build();
        assertThat(caseTemplateApiMapper.toCaseTemplateApiDtoList(Lists.newArrayList(caseTemplateApiEntity)))
            .isNotNull();
    }

    @Test
    @DisplayName("Test the apiTestCaseRequestToApiTestCaseEntity is null method in the CaseTemplateApiMapper")
    void caseTemplateApiEntityToSceneCaseApiEntity_Test() {
        CaseTemplateApiEntity dto = null;
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(dto);
        assertThat(caseTemplateApiMapper.toSceneCaseList(caseTemplateApiList)).size().isEqualTo(1);
    }


}

