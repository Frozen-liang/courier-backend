package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_WORKSPACE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_WORKSPACE_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import com.sms.satp.entity.workspace.WorkspaceEntity;
import com.sms.satp.mapper.WorkspaceMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.WorkspaceRepository;
import com.sms.satp.service.impl.WorkspaceServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for WorkspaceService")
class WorkspaceServiceTest {

    private final WorkspaceRepository workspaceRepository = mock(WorkspaceRepository.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(
        CommonDeleteRepository.class);
    private final WorkspaceMapper workspaceMapper = mock(WorkspaceMapper.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private final WorkspaceService workspaceService = new WorkspaceServiceImpl(projectService,
        workspaceRepository, commonDeleteRepository, workspaceMapper);
    private final WorkspaceEntity workspace = WorkspaceEntity.builder().id(ID).build();
    private final WorkspaceResponse workspaceResponse = WorkspaceResponse.builder()
        .id(ID).build();
    private final WorkspaceRequest workspaceRequest = WorkspaceRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the Workspace service")
    public void findById_test() {
        when(workspaceRepository.findById(ID)).thenReturn(Optional.of(workspace));
        when(workspaceMapper.toDto(workspace)).thenReturn(workspaceResponse);
        WorkspaceResponse result1 = workspaceService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting Workspace")
    public void findById_exception_test() {
        when(workspaceRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> workspaceService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_WORKSPACE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the Workspace service")
    public void add_test() {
        when(workspaceMapper.toEntity(workspaceRequest)).thenReturn(workspace);
        when(workspaceRepository.insert(any(WorkspaceEntity.class))).thenReturn(workspace);
        assertThat(workspaceService.add(workspaceRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding Workspace")
    public void add_exception_test() {
        when(workspaceMapper.toEntity(any())).thenReturn(WorkspaceEntity.builder().build());
        doThrow(new RuntimeException()).when(workspaceRepository).insert(any(WorkspaceEntity.class));
        assertThatThrownBy(() -> workspaceService.add(workspaceRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_WORKSPACE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the Workspace service")
    public void edit_test() {
        when(workspaceMapper.toEntity(workspaceRequest)).thenReturn(workspace);
        when(workspaceRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(workspaceRepository.save(any(WorkspaceEntity.class))).thenReturn(workspace);
        assertThat(workspaceService.edit(workspaceRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit Workspace")
    public void edit_exception_test() {
        when(workspaceMapper.toEntity(workspaceRequest)).thenReturn(workspace);
        when(workspaceRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(workspaceRepository).save(any(WorkspaceEntity.class));
        assertThatThrownBy(() -> workspaceService.edit(workspaceRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_WORKSPACE_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit Workspace")
    public void edit_not_exist_exception_test() {
        when(workspaceMapper.toEntity(workspaceRequest)).thenReturn(workspace);
        when(workspaceRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> workspaceService.edit(workspaceRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the Workspace service")
    public void list_test() {
        ArrayList<WorkspaceEntity> workspaceList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceList.add(WorkspaceEntity.builder().build());
        }
        ArrayList<WorkspaceResponse> workspaceResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceResponseList.add(WorkspaceResponse.builder().build());
        }
        when(workspaceRepository.findAllByRemovedIsFalseOrderByCreateDateTimeDesc()).thenReturn(workspaceList);
        when(workspaceMapper.toDtoList(workspaceList)).thenReturn(workspaceResponseList);
        List<WorkspaceResponse> result = workspaceService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting Workspace list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(workspaceRepository).findAllByRemovedIsFalseOrderByCreateDateTimeDesc();
        assertThatThrownBy(workspaceService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_WORKSPACE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the findByUserId method in the Workspace service")
    public void findByUserId_test() {
        ArrayList<WorkspaceEntity> workspaceList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceList.add(WorkspaceEntity.builder().build());
        }
        ArrayList<WorkspaceResponse> workspaceResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceResponseList.add(WorkspaceResponse.builder().build());
        }
        when(workspaceRepository.findAllByRemovedIsFalseAndUserIdsContainsOrderByCreateDateTimeDesc(any()))
            .thenReturn(workspaceList);
        when(workspaceMapper.toDtoList(workspaceList)).thenReturn(workspaceResponseList);
        List<WorkspaceResponse> result = workspaceService.findByUserId();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting Workspace list")
    public void findByUserId_exception_test() {
        doThrow(new RuntimeException()).when(workspaceRepository)
            .findAllByRemovedIsFalseAndUserIdsContainsOrderByCreateDateTimeDesc(any());
        assertThatThrownBy(workspaceService::findByUserId)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test the delete method in the Workspace service")
    public void delete_test() {
        when(commonDeleteRepository.deleteById(ID, WorkspaceEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(workspaceService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Workspace")
    public void delete_exception_test() {

        doThrow(new RuntimeException()).when(commonDeleteRepository)
            .deleteById(ID, WorkspaceEntity.class);
        assertThatThrownBy(() -> workspaceService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_WORKSPACE_BY_ID_ERROR.getCode());
    }

}