package com.sms.courier.mapper;

import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import com.sms.courier.entity.system.LoginSettingEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoginSettingMapper {

    LoginSettingResponse toDto(LoginSettingEntity emailSettings);

    List<LoginSettingResponse> toDtoList(List<LoginSettingEntity> emailSettingsList);

    LoginSettingEntity toEntity(LoginSettingRequest loginSettingRequest);
}
