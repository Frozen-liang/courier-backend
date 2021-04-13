package com.sms.satp.mapper;

import com.sms.satp.entity.dto.GlobalFunctionDto;
import com.sms.satp.entity.function.GlobalFunction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GlobalFunctionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "id", expression = "java(globalFunction.getId().toString())")
    GlobalFunctionDto toDto(GlobalFunction globalFunction);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "id",
        expression = "java(com.sms.satp.utils.ObjectIdConverter.toObjectId(globalFunctionDto.getId()))")
    GlobalFunction toEntity(GlobalFunctionDto globalFunctionDto);
}