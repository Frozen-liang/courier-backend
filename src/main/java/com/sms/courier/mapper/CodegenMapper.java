package com.sms.courier.mapper;

import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.generator.pojo.CodeEntityParamVo;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CodegenMapper {

    List<CodeEntityParamVo> toParamModelList(List<ParamInfoResponse> paramInfoList);

    @Mapping(target = "key", expression = "java(com.sms.courier.utils.CustomStringUtil.formatFirstUpperCase(paramInfo"
        + ".getKey()))")
    @Mapping(target = "oldParamType", expression = "java(com.sms.courier.common.enums.ParamType"
        + ".getType(paramInfo.getParamType()))")
    CodeEntityParamVo toParamModel(ParamInfoResponse paramInfo);

}
