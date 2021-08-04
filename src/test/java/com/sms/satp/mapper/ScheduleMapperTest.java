package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.ScheduleRequest;
import com.sms.satp.dto.response.ScheduleResponse;
import com.sms.satp.entity.schedule.ScheduleEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ScheduleMapper")
class ScheduleMapperTest {

    private ScheduleMapper scheduleMapper = new ScheduleMapperImpl();
    private static final String NAME = "schedule";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Schedule's entity object to a dto object")
    void entity_to_response() {
        ScheduleEntity schedule = ScheduleEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ScheduleResponse scheduleResponse = scheduleMapper.toResponse(schedule);
        assertThat(scheduleResponse.getName()).isEqualTo(NAME);
    }


    @Test
    @DisplayName("Test the method to convert the Schedule's dto object to a entity object")
    void dto_to_entity() {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
            .name(NAME)
            .build();
        ScheduleEntity schedule = scheduleMapper.toEntity(scheduleRequest);
        assertThat(schedule.getName()).isEqualTo(NAME);
    }



}