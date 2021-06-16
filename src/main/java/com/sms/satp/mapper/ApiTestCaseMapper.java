package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.job.common.JobApiTestCase;
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

    @Mapping(target = "responseResultVerificationResponse", source = "responseResultVerification")
    @Mapping(target = "responseHeadersVerificationResponse", source = "responseHeadersVerification")
    ApiTestCaseResponse toDto(ApiTestCase apiTestCase);

    List<ApiTestCaseResponse> toDtoList(List<ApiTestCase> apiTestCaseList);

    ApiTestCase toEntity(ApiTestCaseRequest apiTestCaseRequest);

    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = DEFAULT_PATTERN)
    JobApiTestCase toJob(ApiTestCase apiTestCase);
}