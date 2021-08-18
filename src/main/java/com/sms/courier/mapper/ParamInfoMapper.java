package com.sms.courier.mapper;

import com.sms.courier.dto.request.ParamInfoRequest;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface ParamInfoMapper {

    @Mapping(target = "paramType", expression = "java(com.sms.courier.common.enums.ParamType"
        + ".getType(paramInfoRequest.getParamType()))")
    ParamInfo toEntity(ParamInfoRequest paramInfoRequest);

    List<ParamInfo> toEntityList(List<ParamInfoRequest> paramInfoDtoList);

    ParamInfoResponse toDto(ParamInfo paramInfo);

    List<ParamInfoResponse> toDtoList(List<ParamInfo> paramInfoList);


}