package com.sms.courier.mapper;

import com.sms.courier.dto.request.ProjectFunctionRequest;
import com.sms.courier.dto.response.ProjectFunctionResponse;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ProjectFunctionMapper {

    ProjectFunctionResponse toDto(ProjectFunctionEntity projectFunction);

    List<ProjectFunctionResponse> toDtoList(List<ProjectFunctionEntity> projectFunctions);

    ProjectFunctionEntity toEntity(ProjectFunctionRequest projectFunctionDto);
}