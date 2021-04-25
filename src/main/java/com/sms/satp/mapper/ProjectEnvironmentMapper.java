package com.sms.satp.mapper;

import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import com.sms.satp.entity.env.ProjectEnvironment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectEnvironmentMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ProjectEnvironmentDto toDto(ProjectEnvironment projectEnvironment);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    ProjectEnvironment toEntity(ProjectEnvironmentDto projectEnvironmentDto);
}
