package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddSceneCaseApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.dto.vo.SceneCaseApiVo;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {EnumCommonUtils.class, ApiTestCaseMapper.class, ResponseHeadersVerificationMapper.class,
        ResponseResultVerificationMapper.class,
        ParamInfoMapper.class, MatchParamInfoMapper.class,
        ResponseHeadersVerificationMapper.class, ResponseResultVerificationMapper.class})
public interface SceneCaseApiMapper {

    SceneCaseApiEntity toSceneCaseApiByUpdateRequest(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    SceneCaseApiResponse toSceneCaseApiDto(SceneCaseApiEntity sceneCaseApi);

    List<SceneCaseApiEntity> toSceneCaseApiList(List<UpdateSceneCaseApiRequest> sceneCaseApiList);

    List<SceneCaseApiEntity> toSceneCaseApiListByAddRequest(List<AddSceneCaseApiRequest> addSceneCaseApiRequestList);

    @Mapping(target = "apiTestCase.id", expression = "java(new org.bson.types.ObjectId().toString())")
    SceneCaseApiEntity toSceneCaseApi(AddSceneCaseApiRequest addSceneCaseApiRequest);

    SceneCaseApiConnResponse toSceneCaseApiConnResponse(SceneCaseApiEntity sceneCaseApi);

    List<SceneCaseApiVo> toSceneCaseApiVoList(List<SceneCaseApiEntity> sceneCaseApiEntityList);

    List<SceneCaseApiEntity> toEntityByVoList(List<SceneCaseApiVo> entityList);

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
    SceneCaseApiEntity toEntityByVo(SceneCaseApiVo response);
}
