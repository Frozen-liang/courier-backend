package com.sms.satp.mapper;

import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.entity.dto.DataCollectionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCollectionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    DataCollectionDto toDto(DataCollection dataCollection);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    DataCollection toEntity(DataCollectionDto dataCollectionDto);
}