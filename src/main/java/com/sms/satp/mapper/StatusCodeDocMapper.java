package com.sms.satp.mapper;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface StatusCodeDocMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    StatusCodeDocDto toDto(StatusCodeDoc statusCodeDoc);

    StatusCodeDoc toEntity(StatusCodeDocDto statusCodeDocDto);
}
