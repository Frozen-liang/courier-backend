package com.sms.courier.mapper;

import com.sms.courier.config.EmailProperties;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.utils.AesUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {AesUtil.class})
public interface EmailServiceMapper {

    @Mapping(target = "properties.host", source = "host")
    @Mapping(target = "properties.port", source = "port")
    @Mapping(target = "properties.username", source = "username")
    @Mapping(target = "properties.password",
        expression = "java(AesUtil.encrypt(emailRequest.getPassword()))")
    @Mapping(target = "properties.protocol", source = "protocol")
    @Mapping(target = "properties.defaultEncoding", source = "defaultEncoding")
    @Mapping(target = "properties.properties", source = "properties")
    EmailServiceEntity toEntity(EmailRequest request);

    EmailPropertiesResponse toResponse(EmailProperties properties);

}
