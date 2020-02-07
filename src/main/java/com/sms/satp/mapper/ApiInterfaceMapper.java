package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiInterfaceMapper {

    ApiInterfaceDto toDto(ApiInterface apiInterface);

    // @InheritInverseConfiguration
    ApiInterface toEntity(ApiInterfaceDto apiInterfaceDto);

    List<ApiInterfaceDto> toDtoList(List<ApiInterface> apiInterfaceList);
}
