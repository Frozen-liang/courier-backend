package com.sms.satp.mapper;

import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironmentEntity;
import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ParamInfoMapper.class, EnumCommonUtils.class})
public interface ProjectEnvironmentMapper {


    ProjectEnvironmentResponse toDto(ProjectEnvironmentEntity projectEnvironment);

    ProjectEnvironmentEntity toEntityByGlobal(GlobalEnvironmentEntity globalEnvironment);

    List<ProjectEnvironmentResponse> toDtoList(List<ProjectEnvironmentEntity> projectEnvironments);

    ProjectEnvironmentEntity toEntity(ProjectEnvironmentRequest projectEnvironmentDto);

}
