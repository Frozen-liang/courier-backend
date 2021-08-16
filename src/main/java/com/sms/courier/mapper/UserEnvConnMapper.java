package com.sms.courier.mapper;

import com.sms.courier.dto.request.UserEnvConnRequest;
import com.sms.courier.dto.response.UserEnvConnResponse;
import com.sms.courier.entity.env.UserEnvConnEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEnvConnMapper {

    UserEnvConnEntity toEntity(UserEnvConnRequest request);

    UserEnvConnResponse toResponse(UserEnvConnEntity entity);
}