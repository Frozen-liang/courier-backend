package com.sms.satp.mapper;

import com.sms.satp.entity.ProjectEnvironment;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectEnvironmentMapper {

    @Mappings({
        @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
        @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    ProjectEnvironmentDto toDto(ProjectEnvironment projectEnvironment);

    @InheritInverseConfiguration
    ProjectEnvironment toEntity(ProjectEnvironmentDto projectEnvironmentDto);
}
