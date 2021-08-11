package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddCaseTemplateApiRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ApiTestCaseMapper.class, EnumCommonUtils.class, ParamInfoMapper.class,
        ResponseHeadersVerificationMapper.class, ResponseResultVerificationMapper.class})
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
