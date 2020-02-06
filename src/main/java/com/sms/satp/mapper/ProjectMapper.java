package com.sms.satp.mapper;

import com.sms.satp.entity.Project;
import com.sms.satp.entity.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mappings({
        @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "dd-MM-yyyy HH:mm:ss"),
        @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    })
    ProjectDto toDto(Project project);

    Project toEntity(ProjectDto projectDto);

}