package com.sms.satp.mapper;

import com.sms.satp.entity.dto.CaseTemplateDto;
import com.sms.satp.entity.scenetest.CaseTemplate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateMapper {

    CaseTemplate toAddCaseTemplate(CaseTemplateDto caseTemplateDto);

    CaseTemplate toUpdateCaseTemplate(CaseTemplateDto caseTemplateDto);

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CaseTemplateDto toDto(CaseTemplate caseTemplate);
}
