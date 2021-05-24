package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.dto.request.AddSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for SceneCaseApiMapper")
class SceneCaseApiMapperTest {

    private ResponseResultVerificationMapper responseResultVerificationMapper =
        mock(ResponseResultVerificationMapper.class);
    private ResponseHeadersVerificationMapper responseHeadersVerificationMapper =
        mock(ResponseHeadersVerificationMapper.class);
    private ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private SceneCaseApiMapper sceneCaseApiMapper = new SceneCaseApiMapperImpl(responseResultVerificationMapper,
        responseHeadersVerificationMapper, paramInfoMapper);
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toSceneCaseApiByUpdateRequest method in the SceneCaseApiMapper")
    void toSceneCaseApiByUpdateRequest_test() {
        UpdateSceneCaseApiRequest updateSceneCaseApiRequest = UpdateSceneCaseApiRequest.builder().id(MOCK_ID).build();
        SceneCaseApi sceneCaseApi = sceneCaseApiMapper.toSceneCaseApiByUpdateRequest(updateSceneCaseApiRequest);
        assertThat(sceneCaseApi.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toSceneCaseApiDto method in the SceneCaseApiMapper")
    void toSceneCaseApiDto_test() {
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).sceneCaseId(MOCK_ID)
            .apiType(ApiType.API)
            .apiProtocol(ApiProtocol.HTTPS)
            .requestMethod(RequestMethod.GET)
            .apiRequestParamType(ApiRequestParamType.FORM_DATA)
            .apiResponseJsonType(ApiJsonType.OBJECT)
            .apiRequestJsonType(ApiJsonType.OBJECT)
            .apiBindingStatus(ApiBindingStatus.BINDING)
            .build();
        SceneCaseApiResponse dto = sceneCaseApiMapper.toSceneCaseApiDto(sceneCaseApi);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toSceneCaseApiList method in the SceneCaseApiMapper")
    void toSceneCaseApiList_test() {
        List<UpdateSceneCaseApiRequest> sceneCaseApiList =
            Lists.newArrayList(UpdateSceneCaseApiRequest.builder().id(MOCK_ID).apiId(MOCK_ID).build());
        List<SceneCaseApi> apiList = sceneCaseApiMapper.toSceneCaseApiList(sceneCaseApiList);
        assertThat(apiList.size()).isEqualTo(sceneCaseApiList.size());
    }

    @Test
    @DisplayName("Test the toSceneCaseApiListByAddRequest method in the SceneCaseApiMapper")
    void toSceneCaseApiListByAddRequest_test() {
        List<AddSceneCaseApiRequest> addSceneCaseApiRequestList =
            Lists.newArrayList(AddSceneCaseApiRequest.builder().build());
        List<SceneCaseApi> sceneCaseApiList =
            sceneCaseApiMapper.toSceneCaseApiListByAddRequest(addSceneCaseApiRequestList);
        assertThat(sceneCaseApiList).isNotEmpty();
    }

}
