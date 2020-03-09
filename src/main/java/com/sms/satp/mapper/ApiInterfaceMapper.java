package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiInterfaceMapper {

    ApiInterfaceDto toDto(ApiInterface apiInterface);

    @Mapping(target = "modifyDateTime",  ignore = true)
    @Mapping(target = "createDateTime",  ignore = true)
    ApiInterface toEntity(ApiInterfaceDto apiInterfaceDto);

    @Mapping(target = "groupId",  ignore = true)
    @Mapping(target = "projectId",  ignore = true)
    @Mapping(target = "requestHeaders",  ignore = true)
    @Mapping(target = "responseHeaders",  ignore = true)
    @Mapping(target = "queryParams",  ignore = true)
    @Mapping(target = "pathParams",  ignore = true)
    @Mapping(target = "requestBody",  ignore = true)
    @Mapping(target = "response",  ignore = true)
    ApiInterfaceDto toDtoPage(ApiInterface apiInterfaceDto);
}
