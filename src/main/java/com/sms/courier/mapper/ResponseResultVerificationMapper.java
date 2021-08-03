package com.sms.courier.mapper;

import com.sms.courier.dto.response.ResponseResultVerificationResponse;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class, MatchParamInfoMapper.class})
public interface ResponseResultVerificationMapper {

    ResponseResultVerificationResponse toDto(ResponseResultVerification responseResultVerification);

    @Mapping(target = "resultVerificationType",
        expression = "java(com.sms.courier.common.enums.ResultVerificationType.getType(responseResultVerification"
            + ".getResultVerificationType()))")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType.getType(responseResultVerification"
            + ".getApiResponseJsonType()))")
    @Mapping(target = "verificationElementType",
        expression = "java(com.sms.courier.common.enums.VerificationElementType.getType(responseResultVerification"
            + ".getVerificationElementType()))")
    ResponseResultVerification toEntity(ResponseResultVerificationResponse responseResultVerification);
}
