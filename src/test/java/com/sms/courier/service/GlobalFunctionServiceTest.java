package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_GLOBAL_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_GLOBAL_FUNCTION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_LIST_ERROR;
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
import com.sms.courier.dto.request.GlobalFunctionRequest;
import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.mapper.GlobalFunctionMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.GlobalFunctionRepository;
import com.sms.courier.service.impl.GlobalFunctionServiceImpl;
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

@DisplayName("Tests for GlobalFunctionService")
class GlobalFunctionServiceTest {

    private final GlobalFunctionRepository globalFunctionRepository = mock(GlobalFunctionRepository.class);
    private final GlobalFunctionMapper globalFunctionMapper = mock(GlobalFunctionMapper.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final MessageService messageService = mock(MessageService.class);
    private final GlobalFunctionService globalFunctionService = new GlobalFunctionServiceImpl(
        globalFunctionRepository,
        globalFunctionMapper, commonRepository, messageService);
    private final GlobalFunctionEntity globalFunction =
        GlobalFunctionEntity.builder().functionKey("name").id(ID).build();
    private final GlobalFunctionResponse globalFunctionResponse = GlobalFunctionResponse.builder().id(ID).build();
    private final GlobalFunctionRequest globalFunctionRequest =
        GlobalFunctionRequest.builder().functionKey("updateName").id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);
    ;
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String FUNCTION_NAME = "functionName";
    private static final String FUNCTION_DESC = "functionDesc";
    private static final String WORKSPACE_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the GlobalFunction service")
    public void findById_test() {
        when(globalFunctionRepository.findById(ID)).thenReturn(Optional.of(globalFunction));
        when(globalFunctionMapper.toDto(globalFunction)).thenReturn(globalFunctionResponse);
        GlobalFunctionResponse result1 = globalFunctionService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalFunction")
    public void findById_exception_test() {
        when(globalFunctionRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> globalFunctionService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_FUNCTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the GlobalFunction service")
    public void add_test() {
        when(globalFunctionMapper.toEntity(globalFunctionRequest)).thenReturn(globalFunction);
        when(globalFunctionRepository.existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        when(globalFunctionRepository.insert(any(GlobalFunctionEntity.class))).thenReturn(globalFunction);
        doNothing().when(messageService).enginePullFunctionMessage(any());
        Boolean result = globalFunctionService.add(globalFunctionRequest);
        verify(globalFunctionRepository, times(1)).insert(any(GlobalFunctionEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An api test platform exception  occurred while adding GlobalFunction")
    public void add_ApiTestPlatformException_test() {
        doThrow(new RuntimeException()).when(globalFunctionRepository).insert(any(GlobalFunctionEntity.class));
        when(globalFunctionMapper.toEntity(any())).thenReturn(GlobalFunctionEntity.builder().build());
        when(globalFunctionRepository.existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(any(), any()))
            .thenReturn(true);
        doNothing().when(messageService).enginePullFunctionMessage(any());
        assertThatThrownBy(() -> globalFunctionService.add(globalFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_FUNCTION_KEY_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding GlobalFunction")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(globalFunctionRepository).insert(any(GlobalFunctionEntity.class));
        when(globalFunctionMapper.toEntity(any())).thenReturn(GlobalFunctionEntity.builder().build());
        doNothing().when(messageService).enginePullFunctionMessage(any());
        assertThatThrownBy(() -> globalFunctionService.add(globalFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_GLOBAL_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the GlobalFunction service")
    public void edit_test() {
        when(globalFunctionMapper.toEntity(globalFunctionRequest)).thenReturn(globalFunction);
        when(globalFunctionRepository.findById(any())).thenReturn(Optional.of(globalFunction));
        when(globalFunctionRepository.existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        when(globalFunctionRepository.save(any(GlobalFunctionEntity.class))).thenReturn(globalFunction);
        doNothing().when(messageService).enginePullFunctionMessage(any());
        assertThat(globalFunctionService.edit(globalFunctionRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit GlobalFunction")
    public void edit_exception_test() {
        when(globalFunctionMapper.toEntity(globalFunctionRequest)).thenReturn(globalFunction);
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(globalFunctionRepository.findById(any())).thenReturn(Optional.of(globalFunction));
        when(globalFunctionRepository.existsByFunctionKeyAndWorkspaceIdAndRemovedIsFalse(any(), any()))
            .thenReturn(false);
        doThrow(new RuntimeException()).when(globalFunctionRepository).save(any(GlobalFunctionEntity.class));
        assertThatThrownBy(() -> globalFunctionService.edit(globalFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_GLOBAL_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit GlobalFunction")
    public void edit_not_exist_exception_test() {
        when(globalFunctionMapper.toEntity(globalFunctionRequest)).thenReturn(globalFunction);
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(globalFunctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> globalFunctionService.edit(globalFunctionRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the GlobalFunction service")
    public void list_test() {
        ArrayList<GlobalFunctionEntity> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(GlobalFunctionEntity.builder().build());
        }
        ArrayList<GlobalFunctionResponse> globalEnvironmentDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            globalEnvironmentDtos.add(GlobalFunctionResponse.builder().build());
        }
        when(globalFunctionRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(globalFunctionMapper.toDtoList(list)).thenReturn(globalEnvironmentDtos);
        List<GlobalFunctionResponse> result = globalFunctionService.list(WORKSPACE_ID, FUNCTION_NAME, FUNCTION_DESC);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalFunction list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(globalFunctionRepository)
            .findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> globalFunctionService.list(WORKSPACE_ID, FUNCTION_NAME, FUNCTION_DESC))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_FUNCTION_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the GlobalFunction service")
    public void delete_test() {
        doNothing().when(messageService).enginePullFunctionMessage(any());
        when(globalFunctionRepository.findById(any())).thenReturn(Optional.of(globalFunction));
        when(commonRepository.deleteByIds(ID_LIST, GlobalFunctionEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(globalFunctionService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalFunction")
    public void delete_exception_test() {
        when(globalFunctionRepository.findById(any())).thenReturn(Optional.of(globalFunction));
        doThrow(new RuntimeException()).when(commonRepository)
            .deleteByIds(ID_LIST, GlobalFunctionEntity.class);
        assertThatThrownBy(() -> globalFunctionService.delete(ID_LIST))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_GLOBAL_FUNCTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalFunction")
    public void findAll_test() {
        Stream<GlobalFunctionResponse> globalFunctionResponses =
            Stream.of(GlobalFunctionResponse.builder().workspaceId(ID).build());
        when(globalFunctionRepository.findAllByRemovedIsFalse()).thenReturn(globalFunctionResponses);
        Map<String, List<GlobalFunctionResponse>> result = globalFunctionService.findAll();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalFunction")
    public void pullFunction_test() {
        List<GlobalFunctionResponse> globalFunctionResponses =
            List.of(GlobalFunctionResponse.builder().workspaceId(ID).build());
        when(globalFunctionRepository.findAllByIdIn(List.of(ID))).thenReturn(globalFunctionResponses);
        List<GlobalFunctionResponse> result = globalFunctionService.pullFunction(List.of(ID));
        assertThat(result).isNotNull();
    }
}