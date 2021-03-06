package com.sms.courier.mapper;

import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {ResponseResultVerificationMapper.class, ResponseHeadersVerificationMapper.class, ParamInfoMapper.class,
        EnumCommonUtils.class})
public interface ApiTestCaseMapper {

    ApiTestCaseResponse toDto(ApiTestCaseEntity apiTestCase);

    List<ApiTestCaseResponse> toDtoList(List<ApiTestCaseEntity> apiTestCaseList);

    ApiTestCaseEntity toEntity(ApiTestCaseRequest apiTestCaseRequest);

    @Mapping(target = "caseName", source = "apiName")
    @Mapping(target = "apiEntity.id", source = "id")
    @Mapping(target = "apiEntity.apiName", source = "apiName")
    @Mapping(target = "apiEntity.apiPath", source = "apiPath")
    @Mapping(target = "apiEntity.apiProtocol", source = "apiProtocol")
    @Mapping(target = "apiEntity.requestMethod", source = "requestMethod")
    @Mapping(target = "apiEntity.apiRequestParamType", source = "apiRequestParamType")
    @Mapping(target = "apiEntity.apiResponseParamType", source = "apiResponseParamType")
    @Mapping(target = "apiEntity.requestHeaders", source = "requestHeaders")
    @Mapping(target = "apiEntity.responseHeaders", source = "responseHeaders")
    @Mapping(target = "apiEntity.pathParams", source = "pathParams")
    @Mapping(target = "apiEntity.restfulParams", source = "restfulParams")
    @Mapping(target = "apiEntity.requestParams", source = "requestParams")
    @Mapping(target = "apiEntity.responseParams", source = "responseParams")
    @Mapping(target = "apiEntity.apiStatus", source = "apiStatus")
    @Mapping(target = "apiEntity.preInject", source = "preInject")
    @Mapping(target = "apiEntity.postInject", source = "postInject")
    @Mapping(target = "apiEntity.apiResponseJsonType", source = "apiResponseJsonType")
    @Mapping(target = "apiEntity.apiRequestJsonType", source = "apiRequestJsonType")
    ApiTestCaseEntity toEntityByApiEntity(ApiRequest apiEntity);

    ApiEntity toApiEntity(ApiRequest request);

}