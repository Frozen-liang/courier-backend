package com.sms.courier.mapper;

import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.entity.schedule.CourierSchedulerEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class,
    imports = {LabelType.class})
public interface CourierSchedulerMapper {

    @Mapping(target = "destination", constant = "scheduler")
    @Mapping(target = "labelType", expression = "java(LabelType.COURIER_SCHEDULER)")
    ContainerInfo toContainerSetting(CourierSchedulerEntity schedulerEntity);
}