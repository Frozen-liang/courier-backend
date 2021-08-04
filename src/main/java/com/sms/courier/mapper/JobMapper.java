package com.sms.courier.mapper;

import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
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
    uses = {EnumCommonUtils.class, ParamInfoMapper.class, MatchParamInfoMapper.class,
        ResponseResultVerificationMapper.class})
public interface JobMapper {

    JobEnvironment toJobEnvironment(ProjectEnvironmentEntity projectEnvironment);

    JobApiTestCase toJobApiTestCase(ApiTestCaseResponse apiTestCaseResponse);

    JobApiTestCase toJobApiTestCase(ApiTestRequest apiTestRequest);

    JobDataCollection toJobDataCollection(DataCollectionRequest dataCollectionRequest);

    TestData toTestDataEntity(TestDataRequest testDataRequest);

    ApiTestCaseJobResponse toApiTestCaseJobResponse(ApiTestCaseJobEntity apiTestCaseJob);

    @Mapping(target = "testDateTime", source = "createDateTime", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "testUser", source = "createUserName")
    @Mapping(target = "testReport", source = "apiTestCaseJob.apiTestCase.jobApiTestCase.caseReport")
    ApiTestCaseJobPageResponse toApiTestCaseJobPageResponse(ApiTestCaseJobEntity apiTestCaseJob);

    List<JobSceneCaseApi> toJobSceneCaseApiList(List<SceneCaseApiEntity> sceneCaseApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    JobSceneCaseApi toJobSceneCaseApi(SceneCaseApiEntity sceneCaseApi);

    List<JobSceneCaseApi> toJobSceneCaseApiListByTemplate(List<CaseTemplateApiEntity> caseTemplateApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    JobSceneCaseApi toJobSceneCaseApiByTemplate(CaseTemplateApiEntity caseTemplateApiList);

    SceneCaseJobResponse toSceneCaseJobResponse(SceneCaseJobEntity sceneCaseJob);

    ApiTestCaseJobReportResponse toApiTestCaseJobReportResponse(ApiTestCaseJobReport caseJobReport);

    SceneCaseJobReportResponse toSceneCaseJobReportResponse(SceneCaseJobReport caseJobReport);
}
