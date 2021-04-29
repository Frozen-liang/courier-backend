package com.sms.satp.mapper;

import com.sms.satp.dto.ProjectFunctionDto;
import com.sms.satp.entity.function.ProjectFunction;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectFunctionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ProjectFunctionDto toDto(ProjectFunction projectFunction);

    List<ProjectFunctionDto> toDtoList(List<ProjectFunction> projectFunctions);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    ProjectFunction toEntity(ProjectFunctionDto projectFunctionDto);
}