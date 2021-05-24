package com.sms.satp.mapper;

import com.sms.satp.dto.response.MatchParamInfoResponse;
import com.sms.satp.entity.api.common.MatchParamInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchParamInfoMapper {

    @Mapping(target = "matchType", expression = "java(matchParamInfo.getMatchType().getCode())")
    @Mapping(target = "paramType", expression = "java(matchParamInfo.getParamType().getCode())")
    MatchParamInfoResponse toDto(MatchParamInfo matchParamInfo);
}
