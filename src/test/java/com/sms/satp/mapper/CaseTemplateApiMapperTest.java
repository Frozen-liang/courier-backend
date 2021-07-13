package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for CaseTemplateApiMapper")
class CaseTemplateApiMapperTest {

    private ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);

    private CaseTemplateApiMapper caseTemplateApiMapper = new CaseTemplateApiMapperImpl(apiTestCaseMapper);
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toCaseTemplateApiByUpdateRequest method in the CaseTemplateApiMapper")
    void toCaseTemplateApiByUpdateRequest_test() {
        UpdateCaseTemplateApiRequest request = getTemplateDto();
        CaseTemplateApi caseTemplateApi = caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(request);
        assertThat(caseTemplateApi.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiDto method in the CaseTemplateApiMapper")
    void toCaseTemplateApiDto_test() {
        CaseTemplateApi api = getTemplateApi();
        CaseTemplateApiResponse dto = caseTemplateApiMapper.toCaseTemplateApiDto(api);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListByUpdateRequestList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListByUpdateRequestList_test() {
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList = Lists.newArrayList(getTemplateDto());
        List<CaseTemplateApi> apiList = caseTemplateApiMapper
            .toCaseTemplateApiListByUpdateRequestList(updateCaseTemplateApiRequestList);
        assertThat(apiList.size()).isEqualTo(updateCaseTemplateApiRequestList.size());
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiListByAddRequestList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiListByAddRequestList_test() {
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList = Lists.newArrayList(
            AddCaseTemplateApiRequest.builder().build());
        List<CaseTemplateApi> caseTemplateApiList =
            caseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(addCaseTemplateApiRequestList);
        assertThat(caseTemplateApiList).isNotEmpty();
    }

    private CaseTemplateApi getTemplateApi() {
        return CaseTemplateApi.builder()
            .id(MOCK_ID)
            .apiType(ApiType.API)
            .apiTestCase(ApiTestCase.builder()
                .apiProtocol(ApiProtocol.HTTPS)
                .requestMethod(RequestMethod.GET)
                .apiRequestParamType(ApiRequestParamType.FORM_DATA)
                .apiResponseJsonType(ApiJsonType.OBJECT)
                .apiRequestJsonType(ApiJsonType.OBJECT)
                .apiId(MOCK_ID)
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

}
