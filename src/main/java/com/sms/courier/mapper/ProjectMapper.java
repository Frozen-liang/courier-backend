package com.sms.courier.mapper;

import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.utils.EnumCommonUtils;
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