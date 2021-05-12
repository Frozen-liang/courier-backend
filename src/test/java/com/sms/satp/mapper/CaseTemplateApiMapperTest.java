package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.dto.AddCaseTemplateApiRequest;
import com.sms.satp.dto.CaseTemplateApiResponse;
import com.sms.satp.dto.UpdateCaseTemplateApiRequest;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for CaseTemplateApiMapper")
class CaseTemplateApiMapperTest {

    private CaseTemplateApiMapper caseTemplateApiMapper = new CaseTemplateApiMapperImpl();
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toCaseTemplateApi method in the CaseTemplateApiMapper")
    void toCaseTemplateApi_test() {
        CaseTemplateApiResponse dto = getTemplateApiResponse();
        CaseTemplateApi api = caseTemplateApiMapper.toCaseTemplateApi(dto);
        assertThat(api.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toCaseTemplateApiByResponseList method in the CaseTemplateApiMapper")
    void toCaseTemplateApiByResponseList_test() {
        List<CaseTemplateApiResponse> caseTemplateApiResponseList = Lists.newArrayList(getTemplateApiResponse());
        List<CaseTemplateApi> caseTemplateApiList =
            caseTemplateApiMapper.toCaseTemplateApiByResponseList(caseTemplateApiResponseList);
        assertThat(caseTemplateApiList).isNotEmpty();
    }

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
            AddCaseTemplateApiRequest.builder().apiId(MOCK_ID).build());
        List<CaseTemplateApi> caseTemplateApiList =
            caseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(addCaseTemplateApiRequestList);
        assertThat(caseTemplateApiList).isNotEmpty();
    }

    private CaseTemplateApi getTemplateApi() {
        return CaseTemplateApi.builder()
            .id(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .apiId(MOCK_ID)
            .build();
    }

    private UpdateCaseTemplateApiRequest getTemplateDto() {
        return UpdateCaseTemplateApiRequest.builder()
            .id(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .apiId(MOCK_ID)
            .build();
    }

    private CaseTemplateApiResponse getTemplateApiResponse() {
        return CaseTemplateApiResponse.builder()
            .id(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .apiId(MOCK_ID)
            .build();
    }

}
