package com.sms.courier.mapper;

import com.sms.courier.dto.response.RoleResponse;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface RoleMapper {

    RoleResponse toResponse(SystemRoleEntity role);

    List<RoleResponse> toDtoList(List<SystemRoleEntity> roleList);
}