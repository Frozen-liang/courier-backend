package com.sms.satp.mapper;

import com.sms.satp.dto.response.ResponseHeadersVerificationResponse;
import com.sms.satp.entity.api.common.ResponseHeadersVerification;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = MatchParamInfoMapper.class)
public interface ResponseHeadersVerificationMapper {

    ResponseHeadersVerificationResponse toDto(ResponseHeadersVerification responseHeadersVerification);
}
