package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiInterfaceMapper {

    ApiInterfaceDto toDto(ApiInterface apiInterface);

    @Mapping(target = "modifyDateTime",  ignore = true)
    @Mapping(target = "createDateTime",  ignore = true)
    ApiInterface toEntity(ApiInterfaceDto apiInterfaceDto);

    List<ApiInterfaceDto> toDtoList(List<ApiInterface> apiInterfaceList);
}
