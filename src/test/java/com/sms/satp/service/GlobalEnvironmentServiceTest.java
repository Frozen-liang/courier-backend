package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.mapper.GlobalEnvironmentMapper;
import com.sms.satp.repository.GlobalEnvironmentRepository;
import com.sms.satp.service.impl.GlobalEnvironmentServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for GlobalEnvironmentService")
class GlobalEnvironmentServiceTest {

    private final GlobalEnvironmentRepository globalEnvironmentRepository = mock(GlobalEnvironmentRepository.class);
    private final GlobalEnvironmentMapper globalEnvironmentMapper = mock(GlobalEnvironmentMapper.class);
    private final GlobalEnvironmentService globalEnvironmentService = new GlobalEnvironmentServiceImpl(
        globalEnvironmentRepository,
        globalEnvironmentMapper);
    private final GlobalEnvironment globalEnvironment = GlobalEnvironment.builder().id(ID).build();
    private final GlobalEnvironmentDto globalEnvironmentDto = GlobalEnvironmentDto.builder().id(ID.toString()).build();
    private static final ObjectId ID = ObjectId.get();
    private static final ObjectId NOT_EXIST_ID = ObjectId.get();
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the GlobalEnvironment service")
    public void findById_test() {
        when(globalEnvironmentRepository.findById(ID)).thenReturn(Optional.of(globalEnvironment));
        when(globalEnvironmentMapper.toDto(globalEnvironment)).thenReturn(globalEnvironmentDto);
        GlobalEnvironmentDto result1 = globalEnvironmentService.findById(ID);
        GlobalEnvironmentDto result2 = globalEnvironmentService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID.toString());
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalEnvironment")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).findById(ID);
        assertThatThrownBy(() -> globalEnvironmentService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the GlobalEnvironment service")
    public void add_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentDto)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.insert(any(GlobalEnvironment.class))).thenReturn(globalEnvironment);
        globalEnvironmentService.add(globalEnvironmentDto);
        verify(globalEnvironmentRepository, times(1)).insert(any(GlobalEnvironment.class));
    }

    @Test
    @DisplayName("An exception occurred while adding GlobalEnvironment")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).insert(any(GlobalEnvironment.class));
        assertThatThrownBy(() -> globalEnvironmentService.add(globalEnvironmentDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_GLOBAL_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the GlobalEnvironment service")
    public void edit_test() {
        when(globalEnvironmentMapper.toEntity(globalEnvironmentDto)).thenReturn(globalEnvironment);
        when(globalEnvironmentRepository.findById(any()))
            .thenReturn(Optional.of(GlobalEnvironment.builder().id(ID).build()));
        when(globalEnvironmentRepository.save(any(GlobalEnvironment.class))).thenReturn(globalEnvironment);
        globalEnvironmentService.edit(globalEnvironmentDto);
        verify(globalEnvironmentRepository, times(1)).save(any(GlobalEnvironment.class));
    }

    @Test
    @DisplayName("An exception occurred while edit GlobalEnvironment")
    public void edit_exception_test() {
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).save(any(GlobalEnvironment.class));
        assertThatThrownBy(() -> globalEnvironmentService.edit(globalEnvironmentDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_GLOBAL_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the GlobalEnvironment service")
    public void list_test() {
        ArrayList<GlobalEnvironment> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(GlobalEnvironment.builder().build());
        }
        when(globalEnvironmentRepository.findAll(any(Sort.class))).thenReturn(list);
        when(globalEnvironmentMapper.toDto(globalEnvironment)).thenReturn(globalEnvironmentDto);
        List<GlobalEnvironmentDto> result = globalEnvironmentService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting GlobalEnvironment list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(globalEnvironmentRepository).findAll(any(Sort.class));
        assertThatThrownBy(globalEnvironmentService::list).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_GLOBAL_ENVIRONMENT_LIST_ERROR.getCode());
    }

}
