package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ResponseResultVerificationMapper.class, ResponseHeadersVerificationMapper.class, ParamInfoMapper.class})
public interface SceneCaseApiMapper {

    SceneCaseApi toSceneCaseApiByUpdateRequest(UpdateSceneCaseApiRequest updateSceneCaseApiRequest);

    @Mapping(target = "responseResultVerificationResponse", source = "responseResultVerification")
    @Mapping(target = "responseHeadersVerificationResponse", source = "responseHeadersVerification")
    @Mapping(target = "apiType",
        expression = "java(sceneCaseApi.getApiType().getCode())")
    @Mapping(target = "apiProtocol",
        expression = "java(sceneCaseApi.getApiProtocol().getCode())")
    @Mapping(target = "requestMethod",
        expression = "java(sceneCaseApi.getRequestMethod().getCode())")
    @Mapping(target = "apiRequestParamType",
        expression = "java(sceneCaseApi.getApiRequestParamType().getCode())")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(sceneCaseApi.getApiResponseJsonType().getCode())")
    @Mapping(target = "apiRequestJsonType",
        expression = "java(sceneCaseApi.getApiRequestJsonType().getCode())")
    @Mapping(target = "apiBindingStatus",
        expression = "java(sceneCaseApi.getApiBindingStatus().getCode())")
    SceneCaseApiResponse toSceneCaseApiDto(SceneCaseApi sceneCaseApi);

    List<SceneCaseApi> toSceneCaseApiList(List<UpdateSceneCaseApiRequest> sceneCaseApiList);

    List<SceneCaseApi> toSceneCaseApiListByAddRequest(List<AddSceneCaseApiRequest> addSceneCaseApiRequestList);
}
