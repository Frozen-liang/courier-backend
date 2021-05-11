package com.sms.satp.mapper;

import com.sms.satp.dto.ParamInfoRequest;
import com.sms.satp.dto.ParamInfoResponse;
import com.sms.satp.entity.api.common.ParamInfo;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParamInfoMapper {


    @Mapping(target = "paramType", expression = "java(com.sms.satp.common.enums.ParamType"
        + ".getType(paramInfoDto.getParamType()))")
    ParamInfo toEntity(ParamInfoRequest paramInfoDto);

    List<ParamInfo> toEntityList(List<ParamInfoRequest> paramInfoDtoList);

    @Mapping(target = "paramType", expression = "java(paramInfo.getParamType().getCode())")
    ParamInfoResponse toDto(ParamInfo paramInfo);

    List<ParamInfoResponse> toDtoList(List<ParamInfo> paramInfoList);

}