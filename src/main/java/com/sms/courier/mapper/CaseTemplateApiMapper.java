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
    uses = {ApiTestCaseMapper.class, EnumCommonUtils.class, ParamInfoMapper.class, MatchParamInfoMapper.class,
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

    CaseTemplateApiEntity toCaseTemplateApiBySceneCaseApi(SceneCaseApiEntity sceneCaseApi);

    List<CaseTemplateApiResponse> toCaseTemplateApiDtoList(List<CaseTemplateApiEntity> caseTemplateApiList);

    List<CaseTemplateApiEntity> toEntityByResponseList(List<CaseTemplateApiResponse> responseList);

    @Mapping(target = "apiType",
        expression = "java(com.sms.courier.common.enums.ApiType.getApiType(response.getApiType()))")
    @Mapping(target = "apiTestCase.responseParamsExtractionType",
        expression = "java(com.sms.courier.common.enums.ResponseParamsExtractionType.getType(apiTestCaseResponse"
            + ".getResponseParamsExtractionType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiProtocol",
        expression = "java(com.sms.courier.common.enums.ApiProtocol"
            + ".getType(apiResponse.getApiProtocol()))")
    @Mapping(target = "apiTestCase.apiEntity.requestMethod",
        expression = "java(com.sms.courier.common.enums.RequestMethod"
            + ".getType(apiResponse.getRequestMethod()))")
    @Mapping(target = "apiTestCase.apiEntity.apiRequestParamType",
        expression = "java(com.sms.courier.common.enums.ApiRequestParamType"
            + ".getType(apiResponse.getApiRequestParamType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiResponseParamType",
        expression = "java(com.sms.courier.common.enums.ApiRequestParamType"
            + ".getType(apiResponse.getApiResponseParamType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiStatus",
        expression = "java(com.sms.courier.common.enums.ApiStatus"
            + ".getType(apiResponse.getApiStatus()))")
    @Mapping(target = "apiTestCase.apiEntity.apiResponseJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType"
            + ".getType(apiResponse.getApiResponseJsonType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiRequestJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType"
            + ".getType(apiResponse.getApiRequestJsonType()))")
    @Mapping(target = "apiTestCase.apiEntity.requestRawType",
        expression = "java(com.sms.courier.common.enums.RawType"
            + ".getType(apiResponse.getRequestRawType()))")
    @Mapping(target = "apiTestCase.apiEntity.responseRawType",
        expression = "java(com.sms.courier.common.enums.RawType"
            + ".getType(apiResponse.getResponseRawType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiEncodingType",
        expression = "java(com.sms.courier.common.enums.ApiEncodingType"
            + ".getType(apiResponse.getApiEncodingType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiNodeType",
        expression = "java(com.sms.courier.common.enums.ApiNodeType"
            + ".getType(apiResponse.getApiNodeType()))")
    @Mapping(target = "apiTestCase.lastTestResult.isSuccess",
        expression = "java(com.sms.courier.common.enums.ResultType"
            + ".getType(testResultResponse.getIsSuccess()))")
    CaseTemplateApiEntity toEntityByResponse(CaseTemplateApiResponse response);

    List<SceneCaseApiEntity> toSceneCaseApiEntityByResponseList(List<CaseTemplateApiResponse> responseList);

    @Mapping(target = "apiType",
        expression = "java(com.sms.courier.common.enums.ApiType.getApiType(response.getApiType()))")
    @Mapping(target = "apiTestCase.responseParamsExtractionType",
        expression = "java(com.sms.courier.common.enums.ResponseParamsExtractionType.getType(apiTestCaseResponse"
            + ".getResponseParamsExtractionType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiProtocol",
        expression = "java(com.sms.courier.common.enums.ApiProtocol"
            + ".getType(apiResponse.getApiProtocol()))")
    @Mapping(target = "apiTestCase.apiEntity.requestMethod",
        expression = "java(com.sms.courier.common.enums.RequestMethod"
            + ".getType(apiResponse.getRequestMethod()))")
    @Mapping(target = "apiTestCase.apiEntity.apiRequestParamType",
        expression = "java(com.sms.courier.common.enums.ApiRequestParamType"
            + ".getType(apiResponse.getApiRequestParamType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiResponseParamType",
        expression = "java(com.sms.courier.common.enums.ApiRequestParamType"
            + ".getType(apiResponse.getApiResponseParamType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiStatus",
        expression = "java(com.sms.courier.common.enums.ApiStatus"
            + ".getType(apiResponse.getApiStatus()))")
    @Mapping(target = "apiTestCase.apiEntity.apiResponseJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType"
            + ".getType(apiResponse.getApiResponseJsonType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiRequestJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType"
            + ".getType(apiResponse.getApiRequestJsonType()))")
    @Mapping(target = "apiTestCase.apiEntity.requestRawType",
        expression = "java(com.sms.courier.common.enums.RawType"
            + ".getType(apiResponse.getRequestRawType()))")
    @Mapping(target = "apiTestCase.apiEntity.responseRawType",
        expression = "java(com.sms.courier.common.enums.RawType"
            + ".getType(apiResponse.getResponseRawType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiEncodingType",
        expression = "java(com.sms.courier.common.enums.ApiEncodingType"
            + ".getType(apiResponse.getApiEncodingType()))")
    @Mapping(target = "apiTestCase.apiEntity.apiNodeType",
        expression = "java(com.sms.courier.common.enums.ApiNodeType"
            + ".getType(apiResponse.getApiNodeType()))")
    @Mapping(target = "apiTestCase.lastTestResult.isSuccess",
        expression = "java(com.sms.courier.common.enums.ResultType"
            + ".getType(testResultResponse.getIsSuccess()))")
    @Mapping(target = "caseTemplateId", ignore = true)
    @Mapping(target = "sceneCaseId", source = "caseTemplateId")
    SceneCaseApiEntity toSceneCaseApiEntityByResponse(CaseTemplateApiResponse response);

}
