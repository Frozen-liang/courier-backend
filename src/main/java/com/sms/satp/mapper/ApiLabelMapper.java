package com.sms.satp.mapper;

import com.sms.satp.entity.ApiLabel;
import com.sms.satp.entity.dto.ApiLabelDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiLabelMapper {

    @Mapping(target = "id", expression = "java(apiLabel.getId().toString())")
    ApiLabelDto toDto(ApiLabel apiLabel);

    @Mapping(target = "id",
        expression = "java(com.sms.satp.utils.ObjectIdConverter.toObjectId(apiLabelDto.getId()))")
    ApiLabel toEntity(ApiLabelDto apiLabelDto);

}