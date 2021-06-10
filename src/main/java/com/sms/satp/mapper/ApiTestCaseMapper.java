package com.sms.satp.mapper;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.job.common.JobApiTestCase;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ResponseResultVerificationMapper.class, ResponseHeadersVerificationMapper.class, ParamInfoMapper.class})
public interface ApiTestCaseMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "responseResultVerificationResponse", source = "responseResultVerification")
    @Mapping(target = "responseHeadersVerificationResponse", source = "responseHeadersVerification")
    @Mapping(target = "apiProtocol",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getApiProtocol()))")
    @Mapping(target = "requestMethod",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getRequestMethod()))")
    @Mapping(target = "apiRequestParamType",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getApiRequestParamType()))")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getApiResponseJsonType()))")
    @Mapping(target = "apiRequestJsonType",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getApiRequestJsonType()))")
    ApiTestCaseResponse toDto(ApiTestCase apiTestCase);

    List<ApiTestCaseResponse> toDtoList(List<ApiTestCase> apiTestCaseList);

    ApiTestCase toEntity(ApiTestCaseRequest apiTestCaseRequest);

    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "apiProtocol",
        expression = "java(apiTestCase.getApiProtocol().getCode())")
    @Mapping(target = "requestMethod",
        expression = "java(apiTestCase.getRequestMethod().getCode())")
    @Mapping(target = "apiRequestParamType",
        expression = "java(com.sms.satp.utils.EnumCommonUtils.getCode(apiTestCase.getApiRequestParamType()))")
    @Mapping(target = "apiResponseJsonType",
        expression = "java(apiTestCase.getApiResponseJsonType().getCode())")
    @Mapping(target = "apiRequestJsonType",
        expression = "java(apiTestCase.getApiRequestJsonType().getCode())")
    JobApiTestCase toJob(ApiTestCase apiTestCase);
}