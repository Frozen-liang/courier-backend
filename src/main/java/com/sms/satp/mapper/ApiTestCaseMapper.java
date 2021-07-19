package com.sms.satp.mapper;

import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.utils.EnumCommonUtils;
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

    @Mapping(target = "apiId", source = "id")
    ApiTestCaseEntity toEntityByApiEntity(ApiEntity apiEntity);
}