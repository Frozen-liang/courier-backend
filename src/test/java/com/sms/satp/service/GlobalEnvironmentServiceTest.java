package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static com.sms.satp.common.exception.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.mapper.GlobalEnvironmentMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.GlobalEnvironmentRepository;
import com.sms.satp.service.impl.GlobalEnvironmentServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for GlobalEnvironmentService")
class GlobalEnvironmentServiceTest {

    private final GlobalEnvironmentRepository globalEnvironmentRepository = mock(GlobalEnvironmentRepository.class);
    private final GlobalEnvironmentMapper globalEnvironmentMapper = mock(GlobalEnvironmentMapper.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(CommonDeleteRepository.class);
    private final GlobalEnvironmentService globalEnvironmentService = new GlobalEnvironmentServiceImpl(
        globalEnvironmentRepository,
        globalEnvironmentMapper, commonDeleteRepository);
    private final GlobalEnvironment globalEnvironment = GlobalEnvironment.builder().id(ID).build();
    private final GlobalEnvironmentResponse globalEnvironmentResponse = GlobalEnvironmentResponse
        .builder().id(ID).build();
    private final GlobalEnvironmentRequest globalEnvironmentRequest = GlobalEnvironmentRequest
        .builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the GlobalEnvironment service")
    public void findById_test() {
        when(globalEnvironmentRepository.findById(ID)).thenReturn(Optional.of(globalEnvironment));
        when(globalEnvironmentMapper.toDto(globalEnvironment)).thenReturn(globalEnvironmentResponse);
        GlobalEnvironmentResponse result1 = globalEnvironmentService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalEnvironment")
    public void findById_exception_test() {
        when(globalEnvironmentRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> globalEnvironmentService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the GlobalEnvironment service")
    public void add_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentRequest)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.insert(any(GlobalEnvironment.class))).thenReturn(globalEnvironment);
        globalEnvironmentService.add(globalEnvironmentRequest);
        verify(globalEnvironmentRepository, times(1)).insert(any(GlobalEnvironment.class));
    }

    @Test
    @DisplayName("An exception occurred while adding GlobalEnvironment")
    public void add_exception_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentRequest)).thenReturn(globalEnvironment);
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).insert(any(GlobalEnvironment.class));
        assertThatThrownBy(() -> globalEnvironmentService.add(globalEnvironmentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_GLOBAL_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the GlobalEnvironment service")
    public void edit_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentRequest)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(globalEnvironmentRepository.save(any(GlobalEnvironment.class))).thenReturn(globalEnvironment);
        assertThat(globalEnvironmentService.edit(globalEnvironmentRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit GlobalEnvironment")
    public void edit_exception_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentRequest)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).save(any(GlobalEnvironment.class));
        assertThatThrownBy(() -> globalEnvironmentService.edit(globalEnvironmentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_GLOBAL_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit GlobalEnvironment")
    public void edit_not_exist_exception_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentRequest)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> globalEnvironmentService.edit(globalEnvironmentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the GlobalEnvironment service")
    public void list_test() {
        ArrayList<GlobalEnvironment> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(GlobalEnvironment.builder().build());
        }
        ArrayList<GlobalEnvironmentResponse> globalEnvironmentDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            globalEnvironmentDtos.add(GlobalEnvironmentResponse.builder().build());
        }

        when(globalEnvironmentRepository.findByRemovedOrderByCreateDateTimeDesc(Boolean.FALSE)).thenReturn(list);
        when(globalEnvironmentMapper.toDtoList(list)).thenReturn(globalEnvironmentDtos);
        List<GlobalEnvironmentResponse> result = globalEnvironmentService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalEnvironment list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(globalEnvironmentRepository)
            .findByRemovedOrderByCreateDateTimeDesc(Boolean.FALSE);
        assertThatThrownBy(globalEnvironmentService::list).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_ENVIRONMENT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ProjectEnvironment service")
    void delete_test() {
        when(commonDeleteRepository.deleteByIds(ID_LIST, GlobalEnvironment.class)).thenReturn(Boolean.TRUE);
        assertThat(globalEnvironmentService.delete(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete GlobalEnvironment")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(commonDeleteRepository)
            .deleteByIds(ID_LIST, GlobalEnvironment.class);
        assertThatThrownBy(() -> globalEnvironmentService.delete(ID_LIST))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID.getCode());
    }

}
