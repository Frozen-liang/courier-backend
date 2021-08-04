package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for SceneCaseMapper")
class SceneCaseMapperTest {

    private SceneCaseMapper sceneCaseMapper = new SceneCaseMapperImpl();
    private static final String MOCK_ID = "1";
    private static final String NAME = "test";

    @Test
    @DisplayName("Test the toAddSceneCase method in the SceneCaseMapper")
    void toAddSceneCase_test() {
        AddSceneCaseRequest dto = AddSceneCaseRequest.builder().groupId(MOCK_ID).name(NAME).build();
        SceneCaseEntity sceneCase = sceneCaseMapper.toAddSceneCase(dto);
        assertThat(sceneCase.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toUpdateSceneCase method in the SceneCaseMapper")
    void toUpdateSceneCase_test() {
        UpdateSceneCaseRequest dto = UpdateSceneCaseRequest.builder().groupId(MOCK_ID).name(NAME).build();
        SceneCaseEntity sceneCase = sceneCaseMapper.toUpdateSceneCase(dto);
        assertThat(sceneCase.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the toDto method in the SceneCaseMapper")
    void toDto_test() {
        SceneCaseEntity sceneCase = SceneCaseEntity.builder().id(MOCK_ID).name(NAME).build();
        SceneCaseResponse dto = sceneCaseMapper.toDto(sceneCase);
        assertThat(dto.getId()).isEqualTo(MOCK_ID);
    }

}
