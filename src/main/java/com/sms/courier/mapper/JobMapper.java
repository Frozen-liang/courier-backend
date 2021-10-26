package com.sms.courier.mapper;

import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.CaseReport;
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

    JobEnvironment toJobEnvironment(GlobalEnvironmentEntity globalEnvironmentEntity);

    @Mapping(target = "jobApi", source = "apiEntity")
    JobApiTestCase toJobApiTestCase(ApiTestCaseEntity apiTestCaseEntity);

    @Mapping(target = "jobApi.id", source = "apiId")
    @Mapping(target = "jobApi.projectId", source = "projectId")
    @Mapping(target = "jobApi.apiName", source = "apiName")
    @Mapping(target = "jobApi.description", source = "description")
    @Mapping(target = "jobApi.apiPath", source = "apiPath")
    @Mapping(target = "jobApi.apiProtocol",
        expression = "java(com.sms.courier.common.enums.ApiProtocol.getType(apiTestRequest.getApiProtocol()))")
    @Mapping(target = "jobApi.requestMethod",
        expression = "java(com.sms.courier.common.enums.RequestMethod.getType(apiTestRequest.getRequestMethod()))")
    @Mapping(target = "jobApi.apiRequestParamType",
        expression =
            "java(com.sms.courier.common.enums.ApiRequestParamType.getType(apiTestRequest.getApiRequestParamType()))")
    @Mapping(target = "jobApi.apiResponseParamType",
        expression =
            "java(com.sms.courier.common.enums.ApiRequestParamType.getType(apiTestRequest.getApiResponseParamType()))")
    @Mapping(target = "jobApi.requestRaw", source = "requestRaw")
    @Mapping(target = "jobApi.requestRawType",
        expression = "java(com.sms.courier.common.enums.RawType.getType(apiTestRequest.getRequestRawType()))")
    @Mapping(target = "jobApi.requestHeaders", source = "requestHeaders")
    @Mapping(target = "jobApi.responseHeaders", source = "responseHeaders")
    @Mapping(target = "jobApi.pathParams", source = "pathParams")
    @Mapping(target = "jobApi.restfulParams", source = "restfulParams")
    @Mapping(target = "jobApi.requestParams", source = "requestParams")
    @Mapping(target = "jobApi.responseParams", source = "responseParams")
    @Mapping(target = "jobApi.preInject", source = "preInject")
    @Mapping(target = "jobApi.postInject", source = "postInject")
    @Mapping(target = "jobApi.apiResponseJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType.getType(apiTestRequest.getApiResponseParamType()))")
    @Mapping(target = "jobApi.apiRequestJsonType",
        expression = "java(com.sms.courier.common.enums.ApiJsonType.getType(apiTestRequest.getApiRequestJsonType()))")
    @Mapping(target = "jobApi.responseRaw", source = "responseRaw")
    @Mapping(target = "jobApi.responseRawType",
        expression = "java(com.sms.courier.common.enums.RawType.getType(apiTestRequest.getRequestRawType()))")
    JobApiTestCase toJobApiTestCase(ApiTestRequest apiTestRequest);

    JobDataCollection toJobDataCollection(DataCollectionRequest dataCollectionRequest);

    TestData toTestDataEntity(TestDataRequest testDataRequest);

    @Mapping(target = "jobType", expression = "java(com.sms.courier.common.enums.JobType.CASE)")
    ApiTestCaseJobResponse toApiTestCaseJobResponse(ApiTestCaseJobEntity caseJobEntity);

    @Mapping(target = "jobType", expression = "java(com.sms.courier.common.enums.JobType.SCHEDULE_CATE)")
    ApiTestCaseJobResponse toScheduleCaseJobResponse(ScheduleCaseJobEntity caseJobEntity);

    @Mapping(target = "testDateTime", source = "createDateTime", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "testUser", source = "createUserName")
    @Mapping(target = "testReport", source = "apiTestCaseJob.apiTestCase.jobApiTestCase.caseReport")
    @Mapping(target = "httpStatusVerification",
        source = "apiTestCaseJob.apiTestCase.jobApiTestCase.httpStatusVerification")
    @Mapping(target = "responseHeadersVerification",
        source = "apiTestCaseJob.apiTestCase.jobApiTestCase.responseHeadersVerification")
    @Mapping(target = "responseResultVerification",
        source = "apiTestCaseJob.apiTestCase.jobApiTestCase.responseResultVerification")
    @Mapping(target = "responseTimeVerification",
        source = "apiTestCaseJob.apiTestCase.jobApiTestCase.responseTimeVerification")
    ApiTestCaseJobPageResponse toApiTestCaseJobPageResponse(ApiTestCaseJobEntity apiTestCaseJob);

    List<JobSceneCaseApi> toJobSceneCaseApiList(List<SceneCaseApiEntity> sceneCaseApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    @Mapping(target = "caseId", expression = "java(org.bson.types.ObjectId.get().toString())")
    JobSceneCaseApi toJobSceneCaseApi(SceneCaseApiEntity sceneCaseApi);

    List<JobSceneCaseApi> toJobSceneCaseApiListByTemplate(List<CaseTemplateApiEntity> caseTemplateApiList);

    @Mapping(target = "jobApiTestCase", source = "apiTestCase")
    @Mapping(target = "caseId", expression = "java(org.bson.types.ObjectId.get().toString())")
    JobSceneCaseApi toJobSceneCaseApiByTemplate(CaseTemplateApiEntity caseTemplateApiList);

    @Mapping(target = "jobType", expression = "java(com.sms.courier.common.enums.JobType.SCENE_CASE)")
    SceneCaseJobResponse toSceneCaseJobResponse(SceneCaseJobEntity sceneCaseJob);

    @Mapping(target = "jobType", expression = "java(com.sms.courier.common.enums.JobType.SCHEDULER_SCENE_CASE)")
    SceneCaseJobResponse toScheduleSceneCaseJobResponse(ScheduleSceneCaseJobEntity sceneCaseJob);

    ApiTestCaseJobReportResponse toApiTestCaseJobReportResponse(ApiTestCaseJobReport caseJobReport);

    SceneCaseJobReportResponse toSceneCaseJobReportResponse(SceneCaseJobReport caseJobReport);

    TestResult toTestResult(CaseReport caseReport);

    List<SceneCaseJobResponse> toSceneCaseJobResponseList(List<SceneCaseJobEntity> jobEntityList);

}
