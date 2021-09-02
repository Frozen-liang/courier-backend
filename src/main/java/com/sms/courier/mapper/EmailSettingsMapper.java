package com.sms.courier.mapper;

import com.sms.courier.dto.request.EmailSettingsRequest;
import com.sms.courier.dto.response.EmailSettingsResponse;
import com.sms.courier.entity.system.EmailSettingsEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailSettingsMapper {

    EmailSettingsResponse toDto(EmailSettingsEntity emailSettings);

    List<EmailSettingsResponse> toDtoList(List<EmailSettingsEntity> emailSettingsList);

    EmailSettingsEntity toEntity(EmailSettingsRequest emailSettingsRequest);
}
