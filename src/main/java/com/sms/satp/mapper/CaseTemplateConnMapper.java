package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.dto.response.CaseTemplateConnResponse;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTemplateConnMapper {

    CaseTemplateConnResponse toCaseTemplateConnDto(CaseTemplateConn conn);

    List<CaseTemplateConn> toCaseTemplateConnList(List<CaseTemplateConnResponse> caseTemplateConnDtoList);

    CaseTemplateConn toCaseTemplateConn(AddCaseTemplateConnRequest addCaseTemplateConnRequest);
}
