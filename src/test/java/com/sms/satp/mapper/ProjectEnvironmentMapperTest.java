package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ProjectEnvironmentMapper")
class ProjectEnvironmentMapperTest {

    private ProjectEnvironmentMapper projectEnvironmentMapper = new ProjectEnvironmentMapperImpl(new ParamInfoMapperImpl());

    private static final Integer SIZE = 10;
    private static final String ENV_DESC = "projectEnvironment";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ProjectEnvironment's entity object to a dto object")
    void entity_to_dto() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder()
            .envDesc(ENV_DESC)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ProjectEnvironmentResponse projectEnvironmentDto = projectEnvironmentMapper.toDto(projectEnvironment);
        assertThat(projectEnvironmentDto.getEnvDesc()).isEqualTo(ENV_DESC);
    }

    @Test
    @DisplayName("Test the method for converting an ProjectEnvironment entity list object to a dto list object")
    void projectEnvironmentList_to_projectEnvironmentDtoList() {
        List<ProjectEnvironmentEntity> projectEnvironments = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            projectEnvironments.add(ProjectEnvironmentEntity.builder().envDesc(ENV_DESC).build());
        }
        List<ProjectEnvironmentResponse> projectEnvironmentDtoList = projectEnvironmentMapper.toDtoList(projectEnvironments);
        assertThat(projectEnvironmentDtoList).hasSize(SIZE);
        assertThat(projectEnvironmentDtoList).allMatch(result -> StringUtils.equals(result.getEnvDesc(), ENV_DESC));
    }

    @Test
    @DisplayName("Test the method to convert the ProjectEnvironment's dto object to a entity object")
    void dto_to_entity() {
        ProjectEnvironmentRequest projectEnvironmentDto = ProjectEnvironmentRequest.builder()
            .envDesc(ENV_DESC)
            .build();
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentMapper.toEntity(projectEnvironmentDto);
        assertThat(projectEnvironment.getEnvDesc()).isEqualTo(ENV_DESC);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectEnvironment's entity object to a dto object")
    void null_entity_to_dto() {
        ProjectEnvironmentResponse projectEnvironmentDto = projectEnvironmentMapper.toDto(null);
        assertThat(projectEnvironmentDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectEnvironment's dto object to a entity object")
    void null_dto_to_entity() {
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentMapper.toEntity(null);
        assertThat(projectEnvironment).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ProjectEnvironment entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ProjectEnvironmentResponse> projectEnvironmentDtoList = projectEnvironmentMapper.toDtoList(null);
        assertThat(projectEnvironmentDtoList).isNull();
    }

}