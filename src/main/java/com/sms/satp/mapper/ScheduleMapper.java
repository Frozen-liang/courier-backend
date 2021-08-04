package com.sms.satp.mapper;

import com.sms.satp.dto.request.ScheduleRequest;
import com.sms.satp.dto.response.ScheduleResponse;
import com.sms.satp.entity.schedule.ScheduleEntity;
import com.sms.satp.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class})
public interface ScheduleMapper {

    ScheduleEntity toEntity(ScheduleRequest scheduleRequest);

    ScheduleResponse toResponse(ScheduleEntity schedule);

}
