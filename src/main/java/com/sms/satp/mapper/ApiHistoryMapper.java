package com.sms.satp.mapper;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.common.ApiHistoryDetail;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface ApiHistoryMapper {


    ApiHistoryDetail toApiHistoryDetail(ApiEntity apiEntity);
}
