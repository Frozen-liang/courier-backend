package com.sms.satp.mapper;

import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ParamInfoMapper.class, EnumCommonUtils.class})
public interface ApiMapper {

    ApiResponse toDto(ApiEntity apiEntity);

    List<ApiResponse> toDtoList(List<ApiEntity> apiEntityList);

    ApiEntity toEntity(ApiRequest apiRequestDto);

}