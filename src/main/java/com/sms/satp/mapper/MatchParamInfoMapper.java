package com.sms.satp.mapper;

import com.sms.satp.dto.response.MatchParamInfoResponse;
import com.sms.satp.entity.api.common.MatchParamInfo;
import com.sms.satp.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class})
public interface MatchParamInfoMapper {

    MatchParamInfoResponse toDto(MatchParamInfo matchParamInfo);
}
