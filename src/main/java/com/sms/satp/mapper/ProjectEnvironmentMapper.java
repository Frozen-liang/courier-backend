package com.sms.satp.mapper;

import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ParamInfoMapper.class, EnumCommonUtils.class})
public interface ProjectEnvironmentMapper {


    ProjectEnvironmentResponse toDto(ProjectEnvironment projectEnvironment);

    ProjectEnvironment toEntityByGlobal(GlobalEnvironment globalEnvironment);

    List<ProjectEnvironmentResponse> toDtoList(List<ProjectEnvironment> projectEnvironments);

    ProjectEnvironment toEntity(ProjectEnvironmentRequest projectEnvironmentDto);

    JobEnvironment toJobEnvironment(ProjectEnvironment projectEnvironment);
}
