package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.ProjectDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ProjectMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectMapperTest {

    @SpyBean
    ProjectMapper projectMapper;

    private static final String NAME = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void entity_to_dto() {
        Project project = Project.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ProjectDto projectDto = projectMapper.toDto(project);
        assertThat(projectDto.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the Project's dto object to a entity object")
    void dto_to_entity() {
        ProjectDto projectDto = ProjectDto.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        Project project = projectMapper.toEntity(projectDto);
        assertThat(project.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Project's entity object to a dto object")
    void null_entity_to_dto() {
        ProjectDto projectDto = projectMapper.toDto(null);
        assertThat(projectDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the Project's dto object to a entity object")
    void null_dto_to_entity() {
        Project project = projectMapper.toEntity(null);
        assertThat(project).isNull();
    }

}