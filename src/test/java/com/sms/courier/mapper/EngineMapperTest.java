package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.model.EngineMemberEntity;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for EngineMapper")
class EngineMapperTest {

    private EngineMapper engineMapper = new EngineMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "testName";

    @Test
    @DisplayName("Test the method to convert the EngineMember's entity object to a dto object")
    void entity_to_dto() {
        EngineMemberEntity apiTag = EngineMemberEntity.builder()
            .name(NAME)
            .build();
        EngineResponse engineResponse = engineMapper.toResponse(apiTag);
        assertThat(engineResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an EngineMember entity list object to a dto list object")
    void apiTagList_to_apiTagDtoList() {
        List<EngineMemberEntity> engineMemberEntities = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            engineMemberEntities.add(EngineMemberEntity.builder().name(NAME).build());
        }
        List<EngineResponse> engineResponses = engineMapper.toResponseList(engineMemberEntities);
        assertThat(engineResponses).hasSize(SIZE);
        assertThat(engineResponses).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the EngineMember's entity object to a ContainerInfo object")
    void engineMember_to_containerInfo() {
        String containerName = "containerName";
        EngineSettingResponse response = EngineSettingResponse.builder().containerName(containerName).build();
        ContainerInfo containerInfo = engineMapper.toContainerSetting(response);
        assertThat(containerInfo.getContainerName()).isEqualTo(containerName);
    }

}