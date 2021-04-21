package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface ApiInterfaceMapper {

    ApiInterfaceDto toDto(ApiInterface apiInterface);

    ApiInterface toEntity(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto toDtoPage(ApiInterface apiInterface);
}
