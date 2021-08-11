package com.sms.courier.mapper;

import com.sms.courier.dto.request.ResponseHeadersVerificationRequest;
import com.sms.courier.dto.response.ResponseHeadersVerificationResponse;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MatchParamInfoMapper.class)
public interface ResponseHeadersVerificationMapper {

    ResponseHeadersVerificationResponse toDto(ResponseHeadersVerification responseHeadersVerification);

    ResponseHeadersVerification toResponseHeadersVerification(ResponseHeadersVerificationRequest request);
}
