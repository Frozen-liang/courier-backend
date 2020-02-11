package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.ApplicationTests;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.service.StatusCodeDocService;
import com.sms.satp.service.WikiService;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectMapperTest {

    @Autowired
    ProjectMapper projectMapper;

    @MockBean
    ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    ProjectEnvironmentService projectEnvironmentService;

    @MockBean
    StatusCodeDocService statusCodeDocService;

    @MockBean
    WikiService wikiService;

    private static final Integer SIZE = 10;
    private static final String NAME = "title";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();

    @Test
    void entity_to_dto() {
        Project project = Project.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .build();
        ProjectDto projectDto = projectMapper.toDto(project);
        assertThat(StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    void dto_to_entity() {
        ProjectDto projectDto = ProjectDto.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        Project project = projectMapper.toEntity(projectDto);
        assertThat(StringUtils.equals(project.getName(), NAME));
    }

}