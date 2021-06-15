package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.ProjectRequest;
import com.sms.satp.dto.response.ProjectResponse;
import com.sms.satp.entity.project.ProjectEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ProjectMapper")
class ProjectMapperTest {

    private ProjectMapper projectMapper = new ProjectMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "project";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void entity_to_dto() {
        ProjectEntity project = ProjectEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ProjectResponse projectResponse = projectMapper.toDto(project);
        assertThat(projectResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an Project entity list object to a dto list object")
    void projectList_to_projectDtoList() {
        List<ProjectEntity> projects = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            projects.add(ProjectEntity.builder().name(NAME).build());
        }
        List<ProjectResponse> projectResponseList = projectMapper.toDtoList(projects);
        assertThat(projectResponseList).hasSize(SIZE);
        assertThat(projectResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the Project's dto object to a entity object")
    void dto_to_entity() {
        ProjectRequest projectRequest = ProjectRequest.builder()
            .name(NAME)
            .build();
        ProjectEntity project = projectMapper.toEntity(projectRequest);
        assertThat(project.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Project's entity object to a dto object")
    void null_entity_to_dto() {
        ProjectResponse projectResponse = projectMapper.toDto(null);
        assertThat(projectResponse).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Project's dto object to a entity object")
    void null_dto_to_entity() {
        ProjectEntity project = projectMapper.toEntity(null);
        assertThat(project).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an Project entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ProjectResponse> projectDtoList = projectMapper.toDtoList(null);
        assertThat(projectDtoList).isNull();
    }

}