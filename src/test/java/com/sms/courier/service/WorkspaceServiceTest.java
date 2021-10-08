package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceResponse;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.mapper.WorkspaceMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.WorkspaceRepository;
import com.sms.courier.service.impl.WorkspaceServiceImpl;
import com.sms.courier.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;

import static com.sms.courier.common.exception.ErrorCode.ADD_WORKSPACE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_WORKSPACE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_WORKSPACE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("Tests for WorkspaceService")
class WorkspaceServiceTest {

    private final WorkspaceRepository workspaceRepository = mock(WorkspaceRepository.class);
    private final CommonRepository commonRepository = mock(
        CommonRepository.class);
    private final WorkspaceMapper workspaceMapper = mock(WorkspaceMapper.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private final ApiTestCaseService apiTestCaseService = mock(ApiTestCaseService.class);
    private final WorkspaceService workspaceService = new WorkspaceServiceImpl(projectService,
        workspaceRepository, commonRepository, workspaceMapper, apiTestCaseService);
    private final WorkspaceEntity workspace = WorkspaceEntity.builder().id(ID).build();
    private final WorkspaceResponse workspaceResponse = WorkspaceResponse.builder()
        .id(ID).build();
    private final WorkspaceRequest workspaceRequest = WorkspaceRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }


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
        ArrayList<WorkspaceResponse> workspaceResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceResponseList.add(WorkspaceResponse.builder().build());
        }
        when(commonRepository.listLookupUser(anyString(), any(), any(Class.class)))
            .thenReturn(workspaceResponseList);

        List<WorkspaceResponse> result = workspaceService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting Workspace list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).listLookupUser(anyString(), any(), any(Class.class));
        assertThatThrownBy(workspaceService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_WORKSPACE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the findByUserId method in the Workspace service")
    public void findByUserId_test() {
        ArrayList<WorkspaceResponse> workspaceResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            workspaceResponseList.add(WorkspaceResponse.builder().build());
        }
        when(commonRepository.listLookupUser(anyString(), any(), any(Class.class)))
            .thenReturn(workspaceResponseList);
        List<WorkspaceResponse> result = workspaceService.findByUserId();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("Test the delete method in the Workspace service")
    public void delete_test() {
        when(commonRepository.deleteById(ID, WorkspaceEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(workspaceService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Workspace")
    public void delete_exception_test() {

        doThrow(new RuntimeException()).when(commonRepository)
            .deleteById(ID, WorkspaceEntity.class);
        assertThatThrownBy(() -> workspaceService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_WORKSPACE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the caseCount method in the Workspace service")
    public void caseCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(apiTestCaseService.countByProjectIds(any())).thenReturn(1L);
        Long count = workspaceService.caseCount(ID);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test the caseCount method in the Workspace service")
    public void caseCountProjectIsNull_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList();
        when(projectService.list(any())).thenReturn(projectResponses);
        Long count = workspaceService.caseCount(ID);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("An exception occurred while caseCount Workspace")
    public void caseCount_exception_test() {
        when(projectService.list(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceService.caseCount(ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getCase method in the Workspace service")
    public void getCase_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().build());
        when(projectService.list(any())).thenReturn(projectResponses);
        Page<ApiTestCaseResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiTestCaseResponse.builder().build()));
        when(apiTestCaseService.getCasePageByProjectIdsAndCreateDate(any(), any(), any())).thenReturn(page);
        Page<ApiTestCaseResponse> pageDto = workspaceService.getCase(ID, new PageDto());
        assertThat(pageDto.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test the getCase method in the Workspace service")
    public void getCaseProjectIsNull_test() {
        when(projectService.list(any())).thenReturn(Lists.newArrayList());
        Page<ApiTestCaseResponse> pageDto = workspaceService.getCase(ID, new PageDto());
        assertThat(pageDto).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while getCase Workspace")
    public void getCase_exception_test() {
        when(projectService.list(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceService.getCase(ID, new PageDto()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}