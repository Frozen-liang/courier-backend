package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ApiTestCaseMapper.class, EnumCommonUtils.class})
public interface CaseTemplateApiMapper {

    CaseTemplateApiEntity toCaseTemplateApiByUpdateRequest(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    CaseTemplateApiResponse toCaseTemplateApiDto(CaseTemplateApiEntity caseTemplateApi);

    List<CaseTemplateApiEntity> toCaseTemplateApiListByUpdateRequestList(
        List<UpdateCaseTemplateApiRequest> updateCaseTemplateApiRequestList);

    List<CaseTemplateApiEntity> toCaseTemplateApiListByAddRequestList(
        List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList);

    @Mapping(target = "apiTestCase.id", expression = "java(new org.bson.types.ObjectId().toString())")
    CaseTemplateApiEntity toCaseTemplateApi(AddCaseTemplateApiRequest addCaseTemplateApiRequest);

    List<SceneCaseApiEntity> toSceneCaseList(List<CaseTemplateApiEntity> caseTemplateApiList);

    List<CaseTemplateApiEntity> toCaseTemplateApiListBySceneCaseApiList(List<SceneCaseApiEntity> sceneCaseApiList);

    @Mapping(target = "id", ignore = true)
    CaseTemplateApiEntity toCaseTemplateApiBySceneCaseApi(SceneCaseApiEntity sceneCaseApi);

    List<CaseTemplateApiResponse> toCaseTemplateApiDtoList(List<CaseTemplateApiEntity> caseTemplateApiList);
}
