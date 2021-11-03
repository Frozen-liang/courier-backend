package com.sms.courier.mapper;

import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.ApiHistoryDetail;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ApiHistoryMapper {

    ApiHistoryDetail toApiHistoryDetail(ApiEntity apiEntity);
}
