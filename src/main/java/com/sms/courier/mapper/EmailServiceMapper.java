package com.sms.courier.mapper;

import com.sms.courier.config.EmailProperties;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.dto.response.EmailConfigurationResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.utils.AesUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailServiceMapper {

    EmailServiceEntity toEntity(EmailRequest request);

    @Mapping(target = "properties.password", ignore = true)
    EmailConfigurationResponse toResponse(EmailServiceEntity entity);

    default EmailProperties encryptProperties(EmailProperties properties) {
        properties.setPassword(AesUtil.encrypt(properties.getPassword()));
        return properties;
    }
}
