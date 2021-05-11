package com.sms.satp.mapper;

import com.sms.satp.dto.ApiRequestDto;
import com.sms.satp.dto.ApiResponseDto;
import com.sms.satp.dto.ParamInfoDto;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.common.ParamInfo;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ApiMapper {

    @Mapping(target = "apiProtocol", expression = "java(apiEntity.getApiProtocol().getCode())")
    @Mapping(target = "requestMethod", expression = "java(apiEntity.getRequestMethod().getCode())")
    @Mapping(target = "apiRequestParamType", expression = "java(apiEntity.getApiRequestParamType().getCode())")
    @Mapping(target = "apiStatus", expression = "java(apiEntity.getApiStatus().getCode())")
    @Mapping(target = "apiResponseJsonType", expression = "java(apiEntity.getApiResponseJsonType().getCode())")
    @Mapping(target = "apiRequestJsonType", expression = "java(apiEntity.getApiRequestJsonType().getCode())")
    ApiResponseDto toDto(ApiEntity apiEntity);

    List<ApiResponseDto> toDtoList(List<ApiEntity> apiEntityList);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "apiProtocol", expression = "java(com.sms.satp.common.enums.ApiProtocol"
        + ".getType(apiRequestDto.getApiProtocol()))")
    @Mapping(target = "requestMethod", expression = "java(com.sms.satp.common.enums.RequestMethod"
        + ".getType(apiRequestDto.getRequestMethod()))")
    @Mapping(target = "apiRequestParamType", expression = "java(com.sms.satp.common.enums.ApiRequestParamType"
        + ".getType(apiRequestDto.getApiRequestParamType()))")
    @Mapping(target = "apiStatus", expression = "java(com.sms.satp.common.enums.ApiStatus"
        + ".getType(apiRequestDto.getApiStatus()))")
    @Mapping(target = "apiResponseJsonType", expression = "java(com.sms.satp.common.enums.ApiJsonType"
        + ".getType(apiRequestDto.getApiResponseJsonType()))")
    @Mapping(target = "apiRequestJsonType", expression = "java(com.sms.satp.common.enums.ApiJsonType"
        + ".getType(apiRequestDto.getApiRequestJsonType()))")
    ApiEntity toEntity(ApiRequestDto apiRequestDto);


}