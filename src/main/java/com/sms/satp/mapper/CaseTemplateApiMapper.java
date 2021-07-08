package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ApiTestCaseMapper.class, EnumCommonUtils.class})
public interface CaseTemplateApiMapper {

    CaseTemplateApi toCaseTemplateApiByUpdateRequest(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    CaseTemplateApiResponse toCaseTemplateApiDto(CaseTemplateApi caseTemplateApi);

    List<CaseTemplateApi> toCaseTemplateApiListByUpdateRequestList(
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList);

    List<CaseTemplateApi> toCaseTemplateApiListByAddRequestList(
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList);

    @Mapping(target = "apiTestCase.id", expression = "java(new org.bson.types.ObjectId().toString())")
    CaseTemplateApi toCaseTemplateApi(AddCaseTemplateApiRequest addCaseTemplateApiRequest);

    List<SceneCaseApi> toSceneCaseList(List<CaseTemplateApi> caseTemplateApiList);

    List<CaseTemplateApi> toCaseTemplateApiListBySceneCaseApiList(List<SceneCaseApi> sceneCaseApiList);

    @Mapping(target = "id", ignore = true)
    CaseTemplateApi toCaseTemplateApiBySceneCaseApi(SceneCaseApi sceneCaseApi);
}
