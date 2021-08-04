package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_FUNCTION_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_FUNCTION_KEY_EXIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ProjectFunctionRequest;
import com.sms.courier.dto.response.FunctionResponse;
import com.sms.courier.dto.response.ProjectFunctionResponse;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.mapper.ProjectFunctionMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectFunctionRepository;
import com.sms.courier.service.impl.ProjectFunctionServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ProjectFunctionService")
class ProjectFunctionServiceTest {

    private final ProjectFunctionRepository projectFunctionRepository = mock(ProjectFunctionRepository.class);
    private final ProjectFunctionMapper projectFunctionMapper = mock(ProjectFunctionMapper.class);
    private final GlobalFunctionService globalFunctionService = mock(GlobalFunctionService.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final MessageService messageService = mock(MessageService.class);
    private final ProjectFunctionService projectFunctionService = new ProjectFunctionServiceImpl(
        projectFunctionRepository, projectFunctionMapper, globalFunctionService, messageService, commonRepository);
    private final ProjectFunctionEntity projectFunction =
        ProjectFunctionEntity.builder().functionKey("name").id(ID).build();
    private final ProjectFunctionResponse projectFunctionResponse = ProjectFunctionResponse
        .builder().id(ID).build();
    private final ProjectFunctionRequest projectFunctionRequest = ProjectFunctionRequest
        .builder().id(ID).functionKey("updateName").build();
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = "10";
    private static final String WORKSPACE_ID = ObjectId.get().toString();
    private static final String FUNCTION_NAME = "functionName";
    private static final String FUNCTION_DESC = "functionDesc";

    @Test
    @DisplayName("Test the findById method in the ProjectFunction service")
    public void findById_test() {
        when(projectFunctionRepository.findById(ID)).thenReturn(Optional.of(projectFunction));
        when(projectFunctionMapper.toDto(projectFunction)).thenReturn(projectFunctionResponse);
        ProjectFunctionResponse result1 = projectFunctionService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectFunction")
    public void findById_exception_test() {
        when(projectFunctionRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> projectFunctionService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_FUNCTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ProjectFunction service")
    public void add_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(projectFunctionRequest)).thenReturn(projectFunction);
        when(projectFunctionRepository.existsByFunctionKeyAndProjectIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        when(projectFunctionRepository.insert(any(ProjectFunctionEntity.class))).thenReturn(projectFunction);
        Boolean result = projectFunctionService.add(projectFunctionRequest);
        verify(projectFunctionRepository, times(1)).insert(any(ProjectFunctionEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An api test platform exception occurred while adding ProjectFunction")
    public void add_ApiTestPlatformException_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(any())).thenReturn(ProjectFunctionEntity.builder().build());
        when(projectFunctionRepository.existsByFunctionKeyAndProjectIdAndRemovedIsFalse(any(), any()))
            .thenReturn(true);
        assertThatThrownBy(() -> projectFunctionService.add(projectFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_FUNCTION_KEY_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding ProjectFunction")
    public void add_exception_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(any())).thenReturn(ProjectFunctionEntity.builder().build());
        doThrow(new RuntimeException()).when(projectFunctionRepository).insert(any(ProjectFunctionEntity.class));
        assertThatThrownBy(() -> projectFunctionService.add(projectFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ProjectFunction service")
    public void edit_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(projectFunctionRequest)).thenReturn(projectFunction);
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.of(projectFunction));
        when(projectFunctionRepository.existsByFunctionKeyAndProjectIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        when(projectFunctionRepository.save(any(ProjectFunctionEntity.class))).thenReturn(projectFunction);
        assertThat(projectFunctionService.edit(projectFunctionRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ProjectFunction")
    public void edit_exception_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(projectFunctionRequest)).thenReturn(projectFunction);
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.of(projectFunction));
        when(projectFunctionRepository.existsByFunctionKeyAndProjectIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        doThrow(new RuntimeException()).when(projectFunctionRepository).save(any(ProjectFunctionEntity.class));
        assertThatThrownBy(() -> projectFunctionService.edit(projectFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ProjectFunction")
    public void edit_not_exist_exception_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(projectFunctionMapper.toEntity(projectFunctionRequest)).thenReturn(projectFunction);
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> projectFunctionService.edit(projectFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ProjectFunction service")
    public void list_test() {
        ArrayList<ProjectFunctionEntity> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ProjectFunctionEntity.builder().build());
        }
        ArrayList<ProjectFunctionResponse> projectEnvironmentDtoList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentDtoList.add(ProjectFunctionResponse.builder().build());
        }
        when(projectFunctionRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(projectFunctionMapper.toDtoList(list)).thenReturn(projectEnvironmentDtoList);
        List<FunctionResponse> result = projectFunctionService
            .list(PROJECT_ID, WORKSPACE_ID, FUNCTION_NAME, FUNCTION_DESC);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectFunction list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(projectFunctionRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> projectFunctionService.list(PROJECT_ID, WORKSPACE_ID, FUNCTION_NAME, FUNCTION_DESC))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_FUNCTION_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ProjectFunction service")
    public void delete_test() {
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.of(projectFunction));
        when(commonRepository.deleteByIds(ID_LIST, ProjectFunctionEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(projectFunctionService.delete(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ProjectFunction")
    public void delete_exception_test() {
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.of(projectFunction));
        doThrow(new RuntimeException()).when(commonRepository)
            .deleteByIds(ID_LIST, ProjectFunctionEntity.class);
        assertThatThrownBy(() -> projectFunctionService.delete(Collections.singletonList(ID)))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_FUNCTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalFunction")
    public void findAll_test() {
        Stream<ProjectFunctionResponse> projectFunctionResponse =
            Stream.of(ProjectFunctionResponse.builder().projectId(ID).build());
        when(projectFunctionRepository.findAllByRemovedIsFalse()).thenReturn(projectFunctionResponse);
        Map<String, List<ProjectFunctionResponse>> result = projectFunctionService.findAll();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalFunction")
    public void pullFunction_test() {
        List<ProjectFunctionResponse> projectFunctionResponses =
            List.of(ProjectFunctionResponse.builder().projectId(ID).build());
        when(projectFunctionRepository.findAllByIdIn(List.of(ID))).thenReturn(projectFunctionResponses);
        List<ProjectFunctionResponse> result = projectFunctionService.pullFunction(List.of(ID));
        assertThat(result).isNotNull();
    }

}