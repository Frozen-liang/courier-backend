package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.satp.dto.response.LogResponse;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface LogMapper {

    @Mapping(target = "operationDateTime", source = "createDateTime", dateFormat = DEFAULT_PATTERN)
    LogResponse toDto(LogEntity logEntity);

    List<LogResponse> toDtoList(List<LogEntity> logList);
}