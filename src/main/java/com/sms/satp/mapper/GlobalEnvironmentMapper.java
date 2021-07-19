package com.sms.satp.mapper;

import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironmentEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ParamInfoMapper.class, EnumCommonUtils.class})
public interface GlobalEnvironmentMapper {

    GlobalEnvironmentResponse toDto(GlobalEnvironmentEntity globalEnvironment);

    List<GlobalEnvironmentResponse> toDtoList(List<GlobalEnvironmentEntity> globalEnvironments);

    GlobalEnvironmentEntity toEntity(GlobalEnvironmentRequest globalEnvironmentDto);
}
