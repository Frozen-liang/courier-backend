package com.sms.courier.mapper;

import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpenApiSettingMapper {

    OpenApiSettingResponse toDto(OpenApiSettingEntity openApiSetting);

    List<OpenApiSettingResponse> toDtoList(List<OpenApiSettingEntity> openApiSettingList);

    OpenApiSettingEntity toEntity(OpenApiSettingRequest openApiSettingRequest);
}
