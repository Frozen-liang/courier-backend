package com.sms.satp.mapper;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StatusCodeDocMapper {

    @Mappings({
        @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
        @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    StatusCodeDocDto toDto(StatusCodeDoc projectEnvironment);

    @InheritInverseConfiguration
    StatusCodeDoc toEntity(StatusCodeDocDto projectEnvironmentDto);
}
