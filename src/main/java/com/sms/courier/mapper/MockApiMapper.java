package com.sms.courier.mapper;

import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ParamInfoMapper.class, MatchParamInfoMapper.class, EnumCommonUtils.class})
public interface MockApiMapper {

    MockApiEntity toEntity(MockApiRequest mockApiRequest);
}
