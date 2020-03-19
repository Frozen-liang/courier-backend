package com.sms.satp.mapper;

import com.sms.satp.entity.DocumentImport;
import com.sms.satp.entity.dto.DocumentImportDto;
import com.sms.satp.mapper.rule.DocumentImportMapperRule;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DocumentImportMapperRule.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DocumentImportMapper {

    @Mapping(target = "content", source = "file")
    DocumentImport convert(DocumentImportDto documentImportDto);
}
