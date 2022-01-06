package com.sms.courier.mapper;

import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface AuthSettingMapper {

    OAuthSettingResponse toDto(OAuthSettingEntity authSetting);

    List<OAuthSettingResponse> toDtoList(List<OAuthSettingEntity> authSettingList);

    OAuthSettingEntity toEntity(OAuthSettingRequest authSettingRequest);
}
