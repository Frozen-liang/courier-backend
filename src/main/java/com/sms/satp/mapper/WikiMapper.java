package com.sms.satp.mapper;

import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.WikiDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WikiMapper {


    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    WikiDto toDto(Wiki wiki);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    Wiki toEntity(WikiDto wikiDto);
}
