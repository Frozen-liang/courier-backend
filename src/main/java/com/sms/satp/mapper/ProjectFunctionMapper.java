package com.sms.satp.mapper;

import com.sms.satp.entity.dto.ProjectFunctionDto;
import com.sms.satp.entity.function.ProjectFunction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectFunctionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "id", expression = "java(projectFunction.getId().toString())")
    ProjectFunctionDto toDto(ProjectFunction projectFunction);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "id",
        expression = "java(com.sms.satp.utils.ObjectIdConverter.toObjectId(projectFunctionDto.getId()))")
    ProjectFunction toEntity(ProjectFunctionDto projectFunctionDto);
}