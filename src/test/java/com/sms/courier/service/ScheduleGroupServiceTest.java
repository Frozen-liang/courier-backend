package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCHEDULE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCHEDULE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.entity.group.ScheduleGroupEntity;
import com.sms.courier.mapper.ScheduleGroupMapper;
import com.sms.courier.mapper.ScheduleGroupMapperImpl;
import com.sms.courier.repository.ScheduleGroupRepository;
import com.sms.courier.service.impl.ScheduleGroupServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ScheduleGroupService")
class ScheduleGroupServiceTest {

    private final ScheduleService scheduleService = mock(ScheduleService.class);
    private final ScheduleGroupRepository scheduleGroupRepository = mock(ScheduleGroupRepository.class);
    private final ScheduleGroupMapper scheduleGroupMapper = new ScheduleGroupMapperImpl();
    private final ScheduleGroupService scheduleGroupService = new ScheduleGroupServiceImpl(
        scheduleService, scheduleGroupRepository, scheduleGroupMapper);
    private final ScheduleGroupEntity scheduleGroup = ScheduleGroupEntity.builder().id(ID).build();
    private final ScheduleGroupResponse scheduleGroupResponse = ScheduleGroupResponse.builder()
        .id(ID).build();
    private final ScheduleGroupRequest scheduleGroupRequest = ScheduleGroupRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the ScheduleGroup service")
    public void findById_test() {
        when(scheduleGroupRepository.findById(ID)).thenReturn(Optional.of(scheduleGroup));
        ScheduleGroupResponse result1 = scheduleGroupService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ScheduleGroup")
    public void findById_exception_test() {
        when(scheduleGroupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> scheduleGroupService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEDULE_GROUP_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ScheduleGroup service")
    public void add_test() {
        when(scheduleGroupRepository.insert(any(ScheduleGroupEntity.class))).thenReturn(scheduleGroup);
        assertThat(scheduleGroupService.add(scheduleGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ScheduleGroup")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(scheduleGroupRepository).insert(any(ScheduleGroupEntity.class));
        assertThatThrownBy(() -> scheduleGroupService.add(scheduleGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_SCHEDULE_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ScheduleGroup service")
    public void edit_test() {
        when(scheduleGroupRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(scheduleGroupRepository.save(any(ScheduleGroupEntity.class))).thenReturn(scheduleGroup);
        assertThat(scheduleGroupService.edit(scheduleGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ScheduleGroup")
    public void edit_exception_test() {
        when(scheduleGroupRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(scheduleGroupRepository).save(any(ScheduleGroupEntity.class));
        assertThatThrownBy(() -> scheduleGroupService.edit(scheduleGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_SCHEDULE_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ScheduleGroup")
    public void edit_not_exist_exception_test() {
        when(scheduleGroupRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> scheduleGroupService.edit(scheduleGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ScheduleGroup service")
    public void list_test() {
        ArrayList<ScheduleGroupResponse> scheduleGroupList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            scheduleGroupList.add(ScheduleGroupResponse.builder().build());
        }
        when(scheduleGroupRepository.findByProjectIdIsOrderByName(PROJECT_ID)).thenReturn(scheduleGroupList);
        List<ScheduleGroupResponse> result = scheduleGroupService.list(PROJECT_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ScheduleGroup list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(scheduleGroupRepository).findByProjectIdIsOrderByName(PROJECT_ID);
        assertThatThrownBy(() -> scheduleGroupService.list(PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEDULE_GROUP_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ScheduleGroup service")
    public void delete_test() {
        doNothing().when(scheduleGroupRepository).deleteById(ID);
        doNothing().when(scheduleService).deleteByGroupId(ID);
        assertThat(scheduleGroupService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ScheduleGroup")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(scheduleGroupRepository)
            .deleteById(ID);
        assertThatThrownBy(() -> scheduleGroupService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_SCHEDULE_GROUP_BY_ID_ERROR.getCode());
    }

}
