package com.sms.courier.mapper;

import com.sms.courier.dto.request.GlobalEnvironmentRequest;
import com.sms.courier.dto.response.GlobalEnvironmentResponse;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.utils.EnumCommonUtils;
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
