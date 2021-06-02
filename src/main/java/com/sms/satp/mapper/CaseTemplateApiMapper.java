package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ApiTestCaseMapper.class)
public interface CaseTemplateApiMapper {

    @Mapping(target = "apiTestCase", source = "apiTestCaseRequest")
    CaseTemplateApi toCaseTemplateApiByUpdateRequest(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    @Mapping(target = "apiTestCaseResponse", source = "apiTestCase")
    @Mapping(target = "apiType",
        expression = "java(caseTemplateApi.getApiType().getCode())")
    @Mapping(target = "apiBindingStatus",
        expression = "java(caseTemplateApi.getApiBindingStatus().getCode())")
    CaseTemplateApiResponse toCaseTemplateApiDto(CaseTemplateApi caseTemplateApi);

    List<CaseTemplateApi> toCaseTemplateApiListByUpdateRequestList(
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList);

    List<CaseTemplateApi> toCaseTemplateApiListByAddRequestList(
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList);

    @Mapping(target = "apiTestCase", source = "apiTestCaseRequest")
    CaseTemplateApi toCaseTemplateApi(AddCaseTemplateApiRequest addCaseTemplateApiRequest);

    List<SceneCaseApi> toSceneCaseList(List<CaseTemplateApi> caseTemplateApiList);
}
