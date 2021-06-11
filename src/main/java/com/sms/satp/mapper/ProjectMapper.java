package com.sms.satp.mapper;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.request.ProjectRequest;
import com.sms.satp.dto.response.ProjectResponse;
import com.sms.satp.entity.project.ProjectEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "type", expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(project.getType()))")
    ProjectResponse toDto(ProjectEntity project);

    List<ProjectResponse> toDtoList(List<ProjectEntity> projectList);

    ProjectEntity toEntity(ProjectRequest projectRequest);
}