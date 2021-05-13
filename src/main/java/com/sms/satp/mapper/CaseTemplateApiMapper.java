package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface CaseTemplateApiMapper {

    CaseTemplateApi toCaseTemplateApi(CaseTemplateApiResponse dto);

    List<CaseTemplateApi> toCaseTemplateApiByResponseList(List<CaseTemplateApiResponse> caseTemplateApiResponseList);

    CaseTemplateApi toCaseTemplateApiByUpdateRequest(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    CaseTemplateApiResponse toCaseTemplateApiDto(CaseTemplateApi sceneCaseTemplateApi);

    List<CaseTemplateApi> toCaseTemplateApiListByUpdateRequestList(
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList);

    List<CaseTemplateApi> toCaseTemplateApiListByAddRequestList(
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList);
}
