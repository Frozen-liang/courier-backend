package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class, ParamInfoMapper.class})
public interface JobMapper {

    JobEnvironment toJobEnvironment(ProjectEnvironment projectEnvironment);

    @Mapping(target = "responseHeadersVerification", source = "responseHeadersVerificationResponse")
    @Mapping(target = "responseResultVerification", source = "responseResultVerificationResponse")
    JobApiTestCase toJobApiTestCase(ApiTestCaseResponse apiTestCaseResponse);

    JobDataCollection toJobDataCollection(DataCollectionRequest dataCollectionRequest);

    TestData toTestDataEntity(TestDataRequest testDataRequest);

    ApiTestCaseJobResponse toApiTestCaseJobResponse(ApiTestCaseJob apiTestCaseJob);

    @Mapping(target = "testDateTime", source = "createDateTime", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "testUser", source = "createUserName")
    @Mapping(target = "testReport", source = "apiTestCaseJob.apiTestCase.jobApiTestCase.caseReport")
    ApiTestCaseJobPageResponse toApiTestCaseJobPageResponse(ApiTestCaseJob apiTestCaseJob);
}
