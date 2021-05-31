package com.sms.satp.mapper;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.entity.env.ProjectEnvironment;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ProjectEnvironmentMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    ProjectEnvironmentResponse toDto(ProjectEnvironment projectEnvironment);

    ProjectEnvironment toEntityByGlobal(GlobalEnvironment globalEnvironment);

    List<ProjectEnvironmentResponse> toDtoList(List<ProjectEnvironment> projectEnvironments);

    ProjectEnvironment toEntity(ProjectEnvironmentRequest projectEnvironmentDto);
}
