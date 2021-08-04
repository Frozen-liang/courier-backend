package com.sms.courier.mapper;

import com.sms.courier.dto.response.MatchParamInfoResponse;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class})
public interface MatchParamInfoMapper {

    MatchParamInfoResponse toDto(MatchParamInfo matchParamInfo);

    @Mapping(target = "matchType",
        expression = "java(com.sms.courier.common.enums.MatchType.getMatchType(matchParamInfo.getMatchType()))")
    @Mapping(target = "paramType",
        expression = "java(com.sms.courier.common.enums.ParamType.getType(matchParamInfo.getParamType()))")
    MatchParamInfo toEntity(MatchParamInfoResponse matchParamInfo);
}
