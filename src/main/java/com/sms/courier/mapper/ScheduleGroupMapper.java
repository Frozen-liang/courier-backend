package com.sms.courier.mapper;

import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.entity.group.ScheduleGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleGroupMapper {

    ScheduleGroupResponse toDto(ScheduleGroupEntity scheduleGroup);

    List<ScheduleGroupResponse> toDtoList(List<ScheduleGroupEntity> scheduleGroupList);

    ScheduleGroupEntity toEntity(ScheduleGroupRequest scheduleGroupRequest);
}
