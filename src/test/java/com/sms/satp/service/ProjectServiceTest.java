package com.sms.satp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.mapper.ProjectMapper;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test the service layer interface of the API Project")
class ProjectServiceTest {

    @MockBean
    private ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    private final static String NAME = "project";
    private final static String PROJECT_ID = "25";
    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;

    @Test
    @DisplayName("Test the query method without query criteria")
    void list_test() {
        Project project;
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            project = Project.builder()
                .name(NAME)
                .build();
            projects.add(project);
        }
        when(projectRepository.findAll()).thenReturn(projects);
        List<ProjectDto> projectList = projectService.list();
        assertThat(projectList.size()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the paging method with no parameters in the project service")
    void page_default_test() {
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectList.add(Project.builder().name(NAME).build());
        }
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, TOTAL_ELEMENTS);
        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        Page<ProjectDto> projectDtoPage = projectService.page(pageDto);
        assertThat(projectDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(projectDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(projectDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the project interface")
    void page_test() {
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectList.add(Project.builder().name(NAME).build());
        }
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, TOTAL_ELEMENTS);
        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        Page<ProjectDto> projectDtoPage = projectService.page(pageDto);
        assertThat(projectDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER);
        assertThat(projectDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(projectDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    @DisplayName("Test the add method in the project service")
    void add_test() {
        ProjectDto projectDto = ProjectDto.builder().build();
        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        when(projectRepository.insert(any(Project.class))).thenReturn(project);
        projectService.add(projectDto);
        verify(projectRepository, times(1)).insert(any(Project.class));
    }

    @Test
    @DisplayName("Test the edit method in the project service")
    void edit_test() {
        ProjectDto projectDto = ProjectDto.builder().build();
        Project project = ProjectMapper.INSTANCE.toEntity(projectDto);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        projectService.edit(projectDto);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Test the delete method in the project service")
    void delete_test() {
        doNothing().when(projectRepository).deleteById(PROJECT_ID);
        projectService.delete(PROJECT_ID);
        verify(projectRepository, times(1)).deleteById(PROJECT_ID);
    }
}