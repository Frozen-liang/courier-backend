package com.sms.courier.mapper;

import com.sms.courier.dto.request.ProjectEnvironmentRequest;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.utils.EnumCommonUtils;
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
