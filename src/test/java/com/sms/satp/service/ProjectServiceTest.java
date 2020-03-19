package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.mapper.ProjectMapper;
import com.sms.satp.repository.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for ProjectService")
class ProjectServiceTest {

    @MockBean
    private ProjectRepository projectRepository;

    @SpyBean
    ProjectService projectService;

    @SpyBean
    ProjectMapper projectMapper;

    private final static String NAME = "project";
    private final static String PROJECT_ID = "25";
    private final static String ID = "25";
    private final static String NOT_EXIST_ID = "30";
    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private static final int FRONT_FIRST_NUMBER = 1;

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
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
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
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectList.add(Project.builder().name(NAME).build());
        }
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, TOTAL_ELEMENTS);
        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        Page<ProjectDto> projectDtoPage = projectService.page(pageDto);
        assertThat(projectDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER - FRONT_FIRST_NUMBER);
        assertThat(projectDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(projectDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    @DisplayName("Test the add method in the project service")
    void add_test() {
        ProjectDto projectDto = ProjectDto.builder().build();
        Project project = projectMapper.toEntity(projectDto);
        when(projectRepository.insert(any(Project.class))).thenReturn(project);
        projectService.add(projectDto);
        verify(projectRepository, times(1)).insert(any(Project.class));
    }

    @Test
    @DisplayName("Test the edit method in the project service")
    void edit_test() {
        ProjectDto projectDto = ProjectDto.builder().id(ID).build();
        Project project = projectMapper.toEntity(projectDto);
        when(projectRepository.findById(ID)).thenReturn(Optional.of(Project.builder().build()));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        projectService.edit(projectDto);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Test the method of querying the Project by id")
    void findById_test() {
        Project project = Project.builder()
            .name(NAME)
            .build();
        Optional<Project> projectEnvironmentOptional = Optional.ofNullable(project);
        when(projectRepository.findById(ID)).thenReturn(projectEnvironmentOptional);
        ProjectDto result1 = projectService.findById(ID);
        ProjectDto result2 = projectService.findById(NOT_EXIST_ID);
        assertThat(result1.getName()).isEqualTo(NAME);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the delete method in the project service")
    void delete_test() {
        doNothing().when(projectRepository).deleteById(PROJECT_ID);
        projectService.delete(PROJECT_ID);
        verify(projectRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    @DisplayName("An exception occurred while getting project list")
    void list_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository).findAll();
        assertThatThrownBy(() -> projectService.list())
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting project page")
    void page_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository).findAll(any(Pageable.class));
        assertThatThrownBy(() -> projectService.page(PageDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding project")
    void add_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository).insert(any(Project.class));
        assertThatThrownBy(() -> projectService.add(ProjectDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit project")
    void edit_exception_test() {
        when(projectRepository.findById(ID)).thenReturn(Optional.of(Project.builder().build()));
        doThrow(new RuntimeException()).when(projectRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> projectService.edit(ProjectDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting projectEnvironment by id")
    void getProjectEnvironment_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository).findById(anyString());
        assertThatThrownBy(() -> projectService.findById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete project")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository).deleteById(anyString());
        assertThatThrownBy(() -> projectService.delete(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_BY_ID_ERROR.getCode());
    }

}