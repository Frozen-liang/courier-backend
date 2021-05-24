package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ResponseResultVerificationMapper.class,
    ResponseHeadersVerificationMapper.class, ParamInfoMapper.class})
public interface CaseTemplateApiMapper {

    CaseTemplateApi toCaseTemplateApiByUpdateRequest(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    @Mapping(target = "responseResultVerificationResponse", source = "responseResultVerification")
    @Mapping(target = "responseHeadersVerificationResponse", source = "responseHeadersVerification")
    @Mapping(target = "apiType",
        expression = "java(caseTemplateApi.getApiType().getCode())")
    @Mapping(target = "apiProtocol",
        expression = "java(caseTemplateApi.getApiProtocol().getCode())")
    @Mapping(target = "requestMethod",
        expression = "java(caseTemplateApi.getRequestMethod().getCode())")
    @Mapping(target = "apiRequestParamType",
        expression = "java(caseTemplateApi.getApiRequestParamType().getCode())")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(caseTemplateApi.getApiResponseJsonType().getCode())")
    @Mapping(target = "apiRequestJsonType",
        expression = "java(caseTemplateApi.getApiRequestJsonType().getCode())")
    @Mapping(target = "apiBindingStatus",
        expression = "java(caseTemplateApi.getApiBindingStatus().getCode())")
    CaseTemplateApiResponse toCaseTemplateApiDto(CaseTemplateApi caseTemplateApi);

    List<CaseTemplateApi> toCaseTemplateApiListByUpdateRequestList(
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList);

    List<CaseTemplateApi> toCaseTemplateApiListByAddRequestList(
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList);
}
