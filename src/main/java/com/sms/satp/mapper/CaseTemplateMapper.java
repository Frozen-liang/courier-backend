package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.SceneCase;
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

    CaseTemplateResponse toDto(CaseTemplate caseTemplate);

    @Mapping(target = "id", ignore = true)
    CaseTemplate toCaseTemplateBySceneCase(SceneCase sceneCase);
}
