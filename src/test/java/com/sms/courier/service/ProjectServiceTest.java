package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_PROJECT_EXIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.mapper.ProjectMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.service.impl.ProjectServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ProjectService")
class ProjectServiceTest {

    private final ProjectRepository projectRepository = mock(ProjectRepository.class);
    private final CommonRepository commonRepository = mock(
        CommonRepository.class);
    private final ProjectMapper projectMapper = mock(ProjectMapper.class);
    private final ProjectService projectService = new ProjectServiceImpl(
        projectRepository, commonRepository, projectMapper);
    private final ProjectEntity project = ProjectEntity.builder().id(ID).build();
    private final ProjectResponse projectResponse = ProjectResponse.builder()
        .id(ID).build();
    private final ProjectRequest projectRequest = ProjectRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String WORKSPACE_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the Project service")
    public void findById_test() {
        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectResponse);
        ProjectResponse result1 = projectService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting Project")
    public void findById_exception_test() {
        when(projectRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> projectService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the Project service")
    public void add_test() {
        when(projectMapper.toEntity(projectRequest)).thenReturn(project);
        when(projectRepository.findOne(any())).thenReturn(Optional.empty());
        when(projectRepository.insert(any(ProjectEntity.class))).thenReturn(project);
        assertThat(projectService.add(projectRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding Project")
    public void add_exception_test() {
        when(projectMapper.toEntity(any())).thenReturn(ProjectEntity.builder().build());
        when(projectRepository.findOne(any())).thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(projectRepository).insert(any(ProjectEntity.class));
        assertThatThrownBy(() -> projectService.add(projectRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding Project")
    public void add_exist_exception_test() {
        when(projectMapper.toEntity(any())).thenReturn(ProjectEntity.builder().build());
        when(projectRepository.findOne(any())).thenReturn(Optional.of(project));
        assertThatThrownBy(() -> projectService.add(projectRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_PROJECT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the Project service")
    public void edit_test() {
        when(projectMapper.toEntity(projectRequest)).thenReturn(project);
        when(projectRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);
        assertThat(projectService.edit(projectRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit Project")
    public void edit_exception_test() {
        when(projectMapper.toEntity(projectRequest)).thenReturn(project);
        when(projectRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(projectRepository).save(any(ProjectEntity.class));
        assertThatThrownBy(() -> projectService.edit(projectRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit Project")
    public void edit_not_exist_exception_test() {
        when(projectMapper.toEntity(projectRequest)).thenReturn(project);
        when(projectRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> projectService.edit(projectRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the Project service")
    public void list_test() {
        ArrayList<ProjectEntity> projectList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectList.add(ProjectEntity.builder().build());
        }
        ArrayList<ProjectResponse> projectResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectResponseList.add(ProjectResponse.builder().build());
        }
        when(projectRepository.findAllByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(any()))
            .thenReturn(projectList);
        when(projectMapper.toDtoList(projectList)).thenReturn(projectResponseList);
        List<ProjectResponse> result = projectService.list(WORKSPACE_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting Project list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(projectRepository)
            .findAllByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(any());
        assertThatThrownBy(() -> projectService.list(WORKSPACE_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the Project service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonRepository.deleteByIds(ids, ProjectEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(projectService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Project")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(commonRepository)
            .deleteByIds(ids, ProjectEntity.class);
        assertThatThrownBy(() -> projectService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_BY_ID_ERROR.getCode());
    }

}