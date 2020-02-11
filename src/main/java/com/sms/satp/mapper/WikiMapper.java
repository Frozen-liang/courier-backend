package com.sms.satp.mapper;

import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.WikiDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WikiMapper {

    @Mappings({
        @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
        @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    WikiDto toDto(Wiki wiki);

    @InheritInverseConfiguration
    Wiki toEntity(WikiDto wikiDto);
}
