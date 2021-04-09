package com.sms.satp.mapper;

import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.env.GlobalEnvironment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GlobalEnvironmentMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "id", expression = "java(globalEnvironment.getId().toString())")
    GlobalEnvironmentDto toDto(GlobalEnvironment globalEnvironment);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "id",
        expression = "java(com.sms.satp.utils.ObjectIdConverter.toObjectId(globalEnvironmentDto.getId()))")
    GlobalEnvironment toEntity(GlobalEnvironmentDto globalEnvironmentDto);
}
