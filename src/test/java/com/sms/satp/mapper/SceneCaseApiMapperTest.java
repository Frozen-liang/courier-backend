package com.sms.satp.mapper;

import com.google.common.collect.Lists;
import com.sms.satp.dto.request.AddSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for SceneCaseApiMapper")
class SceneCaseApiMapperTest {

    private SceneCaseApiMapper sceneCaseApiMapper = new SceneCaseApiMapperImpl();
    private static final String MOCK_ID = "1";

    @Test
    @DisplayName("Test the toSceneCaseApi method in the SceneCaseApiMapper")
    void toSceneCaseApi_test() {
        SceneCaseApiResponse dto = SceneCaseApiResponse.builder().id(MOCK_ID).apiId(MOCK_ID).build();
        SceneCaseApi api = sceneCaseApiMapper.toSceneCaseApi(dto);
        assertThat(api.getId()).isEqualTo(MOCK_ID);
    }

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
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).sceneCaseId(MOCK_ID).build();
        SceneCaseApiResponse dto = sceneCaseApiMapper.toSceneCaseApiDto(sceneCaseApi);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the toSceneCaseApiList method in the SceneCaseApiMapper")
    void toSceneCaseApiList_test() {
        List<SceneCaseApiResponse> sceneCaseApiList =
            Lists.newArrayList(SceneCaseApiResponse.builder().id(MOCK_ID).apiId(MOCK_ID).build());
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
