package com.sms.courier.mapper;

import com.sms.courier.dto.request.ScheduleRequest;
import com.sms.courier.dto.response.ScheduleResponse;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class})
public interface ScheduleMapper {

    ScheduleEntity toEntity(ScheduleRequest scheduleRequest);

    ScheduleResponse toResponse(ScheduleEntity schedule);

}
