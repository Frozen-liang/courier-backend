package com.sms.courier.mapper;

import com.sms.courier.dto.request.ScheduleRequest;
import com.sms.courier.dto.response.ScheduleResponse;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class})
public interface ScheduleMapper {

    @Mapping(target = "executeType", expression = "java(com.sms.courier.common.enums.ExecuteType"
        + ".getExecuteType(scheduleRequest.getExecuteType()))")
    ScheduleEntity toEntity(ScheduleRequest scheduleRequest);

    ScheduleResponse toResponse(ScheduleEntity schedule);

}
