package com.sms.courier.mapper;

import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MockSettingMapper {

    MockSettingEntity toEntity(MockSettingRequest mockSettingRequest);

    MockSettingResponse toResponse(MockSettingEntity mockSettingEntity);
}
