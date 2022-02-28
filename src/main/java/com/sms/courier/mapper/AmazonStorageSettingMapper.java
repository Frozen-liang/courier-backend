package com.sms.courier.mapper;

import com.sms.courier.dto.request.AmazonStorageSettingRequest;
import com.sms.courier.dto.response.AmazonStorageSettingResponse;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AmazonStorageSettingMapper {
    AmazonStorageSettingResponse toDto(AmazonStorageSettingEntity amazonStorageSettingEntity);

    List<AmazonStorageSettingResponse> toDtoList(List<AmazonStorageSettingEntity> amazonStorageSettingSettingsList);

    AmazonStorageSettingEntity toEntity(AmazonStorageSettingRequest amazonStorageSettingRequest);
}
