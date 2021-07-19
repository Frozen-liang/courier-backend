package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateMapper {

    CaseTemplateEntity toCaseTemplate(CaseTemplateResponse caseTemplateDto);

    CaseTemplateEntity toCaseTemplateByUpdateRequest(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    CaseTemplateEntity toCaseTemplateByAddRequest(AddCaseTemplateRequest addCaseTemplateRequest);

    CaseTemplateResponse toDto(CaseTemplateEntity caseTemplate);

    @Mapping(target = "id", ignore = true)
    CaseTemplateEntity toCaseTemplateBySceneCase(SceneCaseEntity sceneCase);
}
