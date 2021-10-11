package com.sms.courier.mapper;

import com.sms.courier.dto.request.ScheduleRecordPageRequest;
import com.sms.courier.dto.response.ScheduleRecordResponse;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface ScheduleRecordMapper {

    ScheduleRecordEntity toEntity(ScheduleRecordPageRequest pageRequest);

    ScheduleRecordResponse toResponse(ScheduleRecordEntity entity);
}