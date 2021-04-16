package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface ApiInterfaceMapper {

    @Mapping(target = "id", expression = "java(apiInterface.getId().toString())")
    ApiInterfaceDto toDto(ApiInterface apiInterface);

    @Mapping(source = "id", target = "id", ignore = true)
    ApiInterface toEntity(ApiInterfaceDto apiInterfaceDto);

    @Mapping(target = "id", expression = "java(apiInterface.getId().toString())")
    ApiInterfaceDto toDtoPage(ApiInterface apiInterface);
}
