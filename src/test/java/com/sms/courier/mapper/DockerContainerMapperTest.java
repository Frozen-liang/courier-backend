package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for DockerContainerMapper")
class DockerContainerMapperTest {

    private DockerContainerMapper dockerContainerMapper = new DockerContainerMapperImpl();

    @Test
    @DisplayName("Test the method to convert the ContainerSetting's entity object to a dto object")
    void entity_to_dto() {
        ContainerSettingEntity entity = ContainerSettingEntity.builder()
            .username("username")
            .build();
        ContainerSettingResponse response = dockerContainerMapper.toResponse(entity);
        assertThat(response.getUsername()).isEqualTo("username");
    }


    @Test
    @DisplayName("Test the method to convert the ContainerSetting's request object to a ContainerSetting entity")
    void engineMember_to_containerInfo() {
        ContainerSettingRequest request = new ContainerSettingRequest();
        String username = "username";
        request.setUsername(username);
        ContainerSettingEntity entity = dockerContainerMapper.toEntity(request);
        assertThat(entity.getUsername()).isEqualTo(username);
    }

}