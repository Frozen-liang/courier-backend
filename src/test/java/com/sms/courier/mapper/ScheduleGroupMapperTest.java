package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.entity.group.ScheduleGroupEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ScheduleGroupMapper")
class ScheduleGroupMapperTest {

    private final ScheduleGroupMapper scheduleGroupMapper = new ScheduleGroupMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "scheduleGroup";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ScheduleGroup's entity object to a dto object")
    void entity_to_dto() {
        ScheduleGroupEntity scheduleGroup = ScheduleGroupEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ScheduleGroupResponse scheduleGroupResponse = scheduleGroupMapper.toDto(scheduleGroup);
        assertThat(scheduleGroupResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an ScheduleGroup entity list object to a dto list object")
    void scheduleGroupList_to_scheduleGroupDtoList() {
        List<ScheduleGroupEntity> scheduleGroups = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            scheduleGroups.add(ScheduleGroupEntity.builder().name(NAME).build());
        }
        List<ScheduleGroupResponse> scheduleGroupResponseList = scheduleGroupMapper.toDtoList(scheduleGroups);
        assertThat(scheduleGroupResponseList).hasSize(SIZE);
        assertThat(scheduleGroupResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ScheduleGroup's dto object to a entity object")
    void dto_to_entity() {
        ScheduleGroupRequest scheduleGroupRequest = ScheduleGroupRequest.builder()
            .name(NAME)
            .build();
        ScheduleGroupEntity scheduleGroup = scheduleGroupMapper.toEntity(scheduleGroupRequest);
        assertThat(scheduleGroup.getName()).isEqualTo(NAME);
    }

}
