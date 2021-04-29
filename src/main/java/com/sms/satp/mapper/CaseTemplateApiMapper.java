package com.sms.satp.mapper;

import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateApiMapper {

    CaseTemplateApi toCaseTemplateApi(CaseTemplateApiDto dto);

    CaseTemplateApiDto toCaseTemplateApiDto(CaseTemplateApi sceneCaseTemplateApi);

    List<CaseTemplateApi> toCaseTemplateApiList(List<CaseTemplateApiDto> caseTemplateApiDtoList);
}
