package com.sms.satp.mapper;

import com.sms.satp.dto.ProjectEnvironmentRequest;
import com.sms.satp.dto.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.ProjectEnvironment;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ProjectEnvironmentMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ProjectEnvironmentResponse toDto(ProjectEnvironment projectEnvironment);

    List<ProjectEnvironmentResponse> toDtoList(List<ProjectEnvironment> projectEnvironments);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    ProjectEnvironment toEntity(ProjectEnvironmentRequest projectEnvironmentDto);
}
