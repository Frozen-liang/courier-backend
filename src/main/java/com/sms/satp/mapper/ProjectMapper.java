package com.sms.satp.mapper;

import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.ProjectDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ProjectDto toDto(Project project);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    Project toEntity(ProjectDto projectDto);

}