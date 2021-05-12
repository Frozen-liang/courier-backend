package com.sms.satp.mapper;

import com.sms.satp.dto.GlobalFunctionRequest;
import com.sms.satp.dto.GlobalFunctionResponse;
import com.sms.satp.entity.function.GlobalFunction;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface GlobalFunctionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    GlobalFunctionResponse toDto(GlobalFunction globalFunction);

    List<GlobalFunctionResponse> toDtoList(List<GlobalFunction> globalFunctions);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    GlobalFunction toEntity(GlobalFunctionRequest globalFunctionDto);
}