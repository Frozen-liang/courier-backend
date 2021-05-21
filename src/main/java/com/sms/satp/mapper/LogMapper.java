package com.sms.satp.mapper;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.response.LogResponse;
import com.sms.satp.entity.log.LogEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogMapper {

    @Mapping(target = "operationType", expression = "java(logEntity.getOperationType().getCode())")
    @Mapping(target = "operationModule", expression = "java(logEntity.getOperationModule().getCode())")
    @Mapping(target = "operationDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    LogResponse toDto(LogEntity logEntity);

    List<LogResponse> toDtoList(List<LogEntity> logList);
}