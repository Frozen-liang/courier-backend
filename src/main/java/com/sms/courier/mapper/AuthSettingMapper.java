package com.sms.courier.mapper;

import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import com.sms.courier.security.oauth.AuthSettingEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface AuthSettingMapper {

    AuthSettingResponse toDto(AuthSettingEntity authSetting);

    List<AuthSettingResponse> toDtoList(List<AuthSettingEntity> authSettingList);

    AuthSettingEntity toEntity(AuthSettingRequest authSettingRequest);
}
