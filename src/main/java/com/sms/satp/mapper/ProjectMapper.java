package com.sms.satp.mapper;

import com.sms.satp.dto.request.ProjectRequest;
import com.sms.satp.dto.response.ProjectResponse;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface ProjectMapper {

    ProjectResponse toDto(ProjectEntity project);

    List<ProjectResponse> toDtoList(List<ProjectEntity> projectList);

    ProjectEntity toEntity(ProjectRequest projectRequest);
}