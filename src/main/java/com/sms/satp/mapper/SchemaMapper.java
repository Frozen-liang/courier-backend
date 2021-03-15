package com.sms.satp.mapper;

import com.sms.satp.entity.Schema;
import com.sms.satp.entity.dto.SchemaDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface SchemaMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SchemaDto toDto(Schema schema);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    Schema toEntity(SchemaDto schemaDto);

}
