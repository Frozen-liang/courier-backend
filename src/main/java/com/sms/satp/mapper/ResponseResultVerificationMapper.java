package com.sms.satp.mapper;

import com.sms.satp.dto.response.ResponseResultVerificationResponse;
import com.sms.satp.entity.api.common.ResponseResultVerification;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MatchParamInfoMapper.class)
public interface ResponseResultVerificationMapper {

    @Mapping(target = "apiResponseJsonType",
        expression = "java(responseResultVerification.getApiResponseJsonType().getCode())")
    ResponseResultVerificationResponse toDto(ResponseResultVerification responseResultVerification);
}
