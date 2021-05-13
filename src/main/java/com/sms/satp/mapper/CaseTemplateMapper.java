package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateMapper {

    CaseTemplate toCaseTemplate(CaseTemplateResponse caseTemplateDto);

    CaseTemplate toCaseTemplateByUpdateRequest(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    CaseTemplate toCaseTemplateByAddRequest(AddCaseTemplateRequest addCaseTemplateRequest);

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CaseTemplateResponse toDto(CaseTemplate caseTemplate);
}
