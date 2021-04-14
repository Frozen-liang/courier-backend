package com.sms.satp.mapper;

import com.sms.satp.entity.ApiLabel;
import com.sms.satp.entity.dto.ApiLabelDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiLabelMapper {

    ApiLabelDto toDto(ApiLabel apiLabel);

    ApiLabel toEntity(ApiLabelDto apiLabelDto);

}