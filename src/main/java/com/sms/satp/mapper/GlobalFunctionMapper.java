package com.sms.satp.mapper;

import com.sms.satp.entity.dto.GlobalFunctionDto;
import com.sms.satp.entity.function.GlobalFunction;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GlobalFunctionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    GlobalFunctionDto toDto(GlobalFunction globalFunction);

    List<GlobalFunctionDto> toDtoList(List<GlobalFunction> globalFunctions);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    GlobalFunction toEntity(GlobalFunctionDto globalFunctionDto);
}