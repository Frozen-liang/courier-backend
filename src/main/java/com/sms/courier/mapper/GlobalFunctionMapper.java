package com.sms.courier.mapper;

import com.sms.courier.dto.request.GlobalFunctionRequest;
import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface GlobalFunctionMapper {

    GlobalFunctionResponse toDto(GlobalFunctionEntity globalFunction);

    List<GlobalFunctionResponse> toDtoList(List<GlobalFunctionEntity> globalFunctions);

    GlobalFunctionEntity toEntity(GlobalFunctionRequest globalFunctionDto);
}