package com.sms.courier.mapper;

import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.listener.event.TestReportEvent;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.JobDatabaseResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobDatabase;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.utils.EnumCommonUtils;
import com.sms.courier.webhook.response.WebhookCaseJobResponse;
import com.sms.courier.webhook.response.WebhookCaseReportResponse;
import com.sms.courier.webhook.response.WebhookSceneCaseJobResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {EnumCommonUtils.class, ParamInfoMapper.class, MatchParamInfoMapper.class,
        ResponseResultVerificationMapper.class},
    imports = {JobStatus.class, NotificationTemplateType.class, ResultType.class, Objects.class})
public interface JobMapper {

    JobEnvironment toJobEnvironment(ProjectEnvironmentEntity projectEnvironment);

    JobEnvironment toJobEnvironment(GlobalEnvironmentEntity globalEnvironmentEntity);

    List<JobEnvironment> toJobEnvironmentList(List<ProjectEnvironmentEntity> projectEnvironmentList);

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
    @Mapping(target = "envName", source = "environment.envName")
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

    JobDatabaseResponse toJobDatabaseResponse(JobDatabase jobDatabase);

    List<SceneCaseJobResponse> toSceneCaseJobResponseList(List<SceneCaseJobEntity> jobEntityList);

    WebhookCaseJobResponse toWebhookCaseJobResponse(ApiTestCaseJobReport jobReport);

    @Mapping(target = "success", expression = "java(jobEntity.getJobStatus() == JobStatus.SUCCESS ? 1 : 0)")
    @Mapping(target = "fail", expression = "java(jobEntity.getJobStatus() == JobStatus.FAIL ? 1 : 0)")
    @Mapping(target = "name", expression = "java(jobEntity.getApiTestCase().getJobApiTestCase().getCaseName())")
    @Mapping(target = "dataName", expression = "java(this.getDataName(jobEntity))")
    @Mapping(target = "type", expression = "java(NotificationTemplateType.TEST_REPORT)")
    TestReportEvent toTestReportEvent(ApiTestCaseJobEntity jobEntity);


    @Mapping(target = "success",
        expression = "java(this.count(ResultType.SUCCESS,caseReports,jobEntity))")
    @Mapping(target = "fail",
        expression = "java(this.count(ResultType.FAIL,caseReports,jobEntity))")
    @Mapping(target = "name", expression = "java(Objects.nonNull(jobEntity) ? jobEntity.getName() : null)")
    @Mapping(target = "dataName", expression = "java(this.getDataName(jobEntity))")
    @Mapping(target = "type", expression = "java(NotificationTemplateType.TEST_REPORT)")
    TestReportEvent toTestReportEvent(SceneCaseJobEntity jobEntity, List<CaseReport> caseReports);

    @Mapping(target = "caseReportList", expression = "java(this.toJsonString(jobReport.getCaseReportList()))")
    WebhookSceneCaseJobResponse toWebhookSceneCaseJobResponse(SceneCaseJobReport jobReport);

    @Mapping(target = "requestHeader",
        expression = "java(this.toJsonString(caseReport.getRequestHeader()))")
    @Mapping(target = "responseHeader",
        expression = "java(this.toJsonString(caseReport.getResponseHeader()))")
    WebhookCaseReportResponse toWebhookCaseReportResponse(CaseReport caseReport) throws IOException;

    default String getDataName(JobEntity jobEntity) {
        if (Objects.nonNull(jobEntity) && Objects.nonNull(jobEntity.getDataCollection())) {
            return jobEntity.getDataCollection().getTestData().getDataName();
        }
        return "";
    }

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default String toJsonString(Object obj) {
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        try {
            if (Objects.nonNull(obj)) {
                return OBJECT_MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    default long count(ResultType resultType, List<CaseReport> caseReports, SceneCaseJobEntity jobEntity) {
        if (Objects.isNull(caseReports)) {
            if (resultType == ResultType.FAIL) {
                return jobEntity.getApiTestCase().size();
            } else {
                return 0L;
            }
        }
        return caseReports.stream().filter(caseReport -> caseReport.getIsSuccess() == resultType).count();
    }


}
