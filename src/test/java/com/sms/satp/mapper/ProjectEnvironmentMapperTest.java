package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.ProjectEnvironment;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ProjectEnvironmentMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectEnvironmentMapperTest {
    
    @SpyBean
    ProjectEnvironmentMapper projectEnvironmentMapper;

    private static final String NAME = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ProjectEnvironment's entity object to a dto object")
    void entity_to_dto() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ProjectEnvironmentDto projectEnvironmentDto = projectEnvironmentMapper.toDto(projectEnvironment);
        assertThat(projectEnvironmentDto.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the ProjectEnvironment's dto object to a entity object")
    void dto_to_entity() {
        ProjectEnvironmentDto projectEnvironmentDto = ProjectEnvironmentDto.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        ProjectEnvironment projectEnvironment = projectEnvironmentMapper.toEntity(projectEnvironmentDto);
        assertThat(projectEnvironment.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectEnvironment's entity object to a dto object")
    void null_entity_to_dto() {
        ProjectEnvironmentDto projectEnvironmentDto = projectEnvironmentMapper.toDto(null);
        assertThat(projectEnvironmentDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectEnvironment's dto object to a entity object")
    void null_dto_to_entity() {
        ProjectEnvironment projectEnvironment = projectEnvironmentMapper.toEntity(null);
        assertThat(projectEnvironment).isNull();
    }

}