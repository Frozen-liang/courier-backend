package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.CycleType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ScheduleListRequest;
import com.sms.courier.dto.request.ScheduleRequest;
import com.sms.courier.dto.response.ScheduleResponse;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.mapper.ScheduleMapper;
import com.sms.courier.mapper.ScheduleMapperImpl;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleRepository;
import com.sms.courier.service.impl.ScheduleServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ScheduleService")
class ScheduleServiceTest {

    private final ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
    private final ScheduleMapper scheduleMapper = new ScheduleMapperImpl();
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ScheduleService scheduleService = new ScheduleServiceImpl(scheduleRepository, commonRepository,
        scheduleMapper);
    private static final String ID = ObjectId.get().toString();
    private final ScheduleEntity schedule =
        ScheduleEntity.builder().id(ID).cycle(CycleType.DAY).time(List.of("11:40")).build();
    private final ScheduleRequest scheduleRequest = ScheduleRequest.builder().cycle(CycleType.DAY).id(ID).build();

    @Test
    @DisplayName("Test the findById method in the Schedule service")
    public void findById_test() {
        when(scheduleRepository.findById(ID)).thenReturn(Optional.of(schedule));
        ScheduleResponse result = scheduleService.findById(ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting Schedule")
    public void findById_exception_test() {
        when(scheduleRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> scheduleService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEDULE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the Schedule service")
    public void add_test() {
        when(scheduleRepository.insert(any(ScheduleEntity.class))).thenReturn(schedule);
        Boolean result = scheduleService.add(scheduleRequest);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding Schedule")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(scheduleRepository).insert(any(ScheduleEntity.class));
        assertThatThrownBy(() -> scheduleService.add(scheduleRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_SCHEDULE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the Schedule service")
    public void edit_test() {
        when(scheduleRepository.findById(anyString())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(ScheduleEntity.class))).thenReturn(schedule);
        Boolean result = scheduleService.edit(scheduleRequest);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit Schedule")
    public void edit_exception_test() {
        when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));
        doThrow(new RuntimeException()).when(scheduleRepository).save(any(ScheduleEntity.class));
        assertThatThrownBy(() -> scheduleService.edit(scheduleRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_SCHEDULE_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit Schedule")
    public void edit_not_exist_exception_test() {
        when(scheduleRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> scheduleService.edit(scheduleRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the Schedule service")
    public void list_test() {
        when(commonRepository.listLookupUser(anyString(), any(), any())).thenReturn(Collections.emptyList());
        assertThat(scheduleService.list(ScheduleListRequest.builder().build())).isNullOrEmpty();
    }

    @Test
    @DisplayName("An exception occurred while getting Schedule list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).listLookupUser(anyString(), any(), any());
        assertThatThrownBy(() -> scheduleService.list(ScheduleListRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEDULE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the Schedule service")
    public void delete_test() {
        when(commonRepository.updateFieldById(anyString(), any(), any())).thenReturn(true);
        assertThat(scheduleService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Schedule")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).updateFieldById(anyString(), any(), any());
        assertThatThrownBy(() -> scheduleService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_SCHEDULE_BY_ID_ERROR.getCode());
    }
}
