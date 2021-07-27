package com.sms.satp.mapper;

import com.sms.satp.dto.response.ResponseResultVerificationResponse;
import com.sms.satp.entity.api.common.ResponseResultVerification;
import com.sms.satp.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class, MatchParamInfoMapper.class})
public interface ResponseResultVerificationMapper {

    ResponseResultVerificationResponse toDto(ResponseResultVerification responseResultVerification);

    @Mapping(target = "resultVerificationType",
        expression = "java(com.sms.satp.common.enums.ResultVerificationType.getType(responseResultVerification"
            + ".getResultVerificationType()))")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(com.sms.satp.common.enums.ApiJsonType.getType(responseResultVerification"
            + ".getApiResponseJsonType()))")
    ResponseResultVerification toEntity(ResponseResultVerificationResponse responseResultVerification);
}
