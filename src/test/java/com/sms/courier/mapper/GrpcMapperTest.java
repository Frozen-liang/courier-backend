package com.sms.courier.mapper;

import static com.sms.courier.entity.job.SceneCaseJobEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.enums.MatchType;
import com.sms.courier.common.enums.ParamType;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.enums.ResponseParamsExtractionType;
import com.sms.courier.common.enums.ResultVerificationType;
import com.sms.courier.common.enums.VerificationElementType;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobReport;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobRequest;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseReport;
import com.sms.courier.engine.grpc.api.v1.GrpcFunction;
import com.sms.courier.engine.grpc.api.v1.GrpcJobApi;
import com.sms.courier.engine.grpc.api.v1.GrpcJobApiTestCase;
import com.sms.courier.engine.grpc.api.v1.GrpcJobSceneCaseApi;
import com.sms.courier.engine.grpc.api.v1.GrpcMatchParamInfo;
import com.sms.courier.engine.grpc.api.v1.GrpcParamInfo;
import com.sms.courier.engine.grpc.api.v1.GrpcResponseResultVerification;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobReport;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobRequest;
import com.sms.courier.entity.api.common.AdvancedSetting;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import com.sms.courier.entity.datacollection.DataParam;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.JobApi;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for GrpcMapper")
public class GrpcMapperTest {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final LocalDateTime DATA = LocalDateTime.parse("2021-12-24T14:51:12.642633500");
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final ParamType PARAM_TYPE = ParamType.getType(1);
    private static final String MOCK_RULE = "mockRule";
    private static final MatchType MATCH_TYPE = MatchType.getMatchType(1);
    private static final String MESSAGE = "MESSAGE";
    private static final String CODE = "CODE";
    private static final String SHELL = "shell";
    private static final Integer ORDER = 1;
    private static final String INJECT = "ject";
    private static final String ENVDESC = "ject";
    private static final String PROCESS = "process";


    //3 创建grpcMapper调用转化方法
    private final GrpcMapper grpcMapper = new GrpcMapperImpl();
    private final JobEnvironment jobEnv = JobEnvironment.builder()
        .id(ID).projectId(ID).envName(NAME).envDesc(ENVDESC).frontUri("frontUri").afterInject("afterInject")
        .beforeInject(INJECT).globalAfterProcess(PROCESS).globalBeforeProcess(PROCESS)
        .requestParams(List.of(ParamInfo.builder().key(KEY).build())).requestParamType(1)
        .headers(List.of(ParamInfo.builder().key(KEY).build()))
        .envVariable(List.of(ParamInfo.builder().key(KEY).build()))
        .urlParams(List.of(ParamInfo.builder().key(KEY).build()))
        .requestParams(List.of(ParamInfo.builder().key(KEY).build())).build();
    private final JobDataCollection jobDataCollection = JobDataCollection.builder()
        .id(ID).projectId(ID).collectionName(NAME).testData(
            TestData.builder().dataName(NAME).data(List.of(DataParam.builder().value(VALUE).key(KEY).build())).build())
        .build();
    private final JobApiTestCase jobApiTestCase = JobApiTestCase.builder().id(ID).build();
    private final JobCaseApi jobCaseApi = JobCaseApi.builder().jobApiTestCase(jobApiTestCase).build();

    @Test
    public void toGrpcJobRequest_SceneCaseJobEntity() {
        SceneCaseJobEntity sceneCaseJobEntity = builder()
            .id(ID).createDateTime(DATA).modifyDateTime(DATA).createUserId(ID).modifyUserId(ID).engineId(ID)
            .projectId(ID).workspaceId(ID).environment(jobEnv).dataCollection(jobDataCollection).createUserName(NAME)
            .apiTestCase(List.of(JobSceneCaseApi.builder().caseId(ID).build())).build();
        GrpcSceneCaseJobRequest grpcSceneCaseJobRequest = grpcMapper.toGrpcJobRequest(sceneCaseJobEntity);
        assertThat(sceneCaseJobEntity.getId()).isEqualTo(grpcSceneCaseJobRequest.getId());
        assertThat(sceneCaseJobEntity.getCreateDateTime()).isEqualTo(grpcSceneCaseJobRequest.getCreateDateTime());
        assertThat(sceneCaseJobEntity.getModifyDateTime()).isEqualTo(grpcSceneCaseJobRequest.getModifyDateTime());
        assertThat(sceneCaseJobEntity.getCreateUserId()).isEqualTo(grpcSceneCaseJobRequest.getCreateUserId());
        assertThat(sceneCaseJobEntity.getModifyUserId()).isEqualTo(grpcSceneCaseJobRequest.getModifyUserId());
        assertThat(sceneCaseJobEntity.getEngineId()).isEqualTo(grpcSceneCaseJobRequest.getEngineId());
        assertThat(sceneCaseJobEntity.getProjectId()).isEqualTo(grpcSceneCaseJobRequest.getProjectId());
        assertThat(sceneCaseJobEntity.getWorkspaceId()).isEqualTo(grpcSceneCaseJobRequest.getWorkspaceId());
        assertThat(sceneCaseJobEntity.getEnvironment().getId())
            .isEqualTo(grpcSceneCaseJobRequest.getEnvironment().getId());
        assertThat(sceneCaseJobEntity.getEnvironment().getProjectId())
            .isEqualTo(grpcSceneCaseJobRequest.getEnvironment().getProjectId());
        assertThat(sceneCaseJobEntity.getEnvironment().getEnvDesc())
            .isEqualTo(grpcSceneCaseJobRequest.getEnvironment().getEnvDesc());
        assertThat(sceneCaseJobEntity.getEnvironment().getHeaders().get(0).getKey())
            .isEqualTo(grpcSceneCaseJobRequest.getEnvironment().getHeaders(0).getKey());
        assertThat(sceneCaseJobEntity.getDataCollection().getId())
            .isEqualTo(grpcSceneCaseJobRequest.getDataCollection().getId());
        assertThat(sceneCaseJobEntity.getDataCollection().getTestData().getData().get(0).getValue())
            .isEqualTo(grpcSceneCaseJobRequest.getDataCollection().getTestData().getData(0).getValue());
        assertThat(sceneCaseJobEntity.getDataCollection().getTestData().getData().get(0).getKey())
            .isEqualTo(grpcSceneCaseJobRequest.getDataCollection().getTestData().getData(0).getKey());
        assertThat(sceneCaseJobEntity.getCreateUserName()).isEqualTo(grpcSceneCaseJobRequest.getCreateUserName());
        assertThat(grpcSceneCaseJobRequest.getApiTestCase(0).getCaseId())
            .isEqualTo(sceneCaseJobEntity.getApiTestCase().get(0).getCaseId());

    }

    @Test
    public void toGrpcJobRequest_ScheduleSceneCaseJobEntity() {
        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity = ScheduleSceneCaseJobEntity
            .builder().id(ID).createDateTime(DATA).modifyDateTime(DATA).engineId(ID).workspaceId(ID).projectId(ID)
            .environment(jobEnv).dataCollection(jobDataCollection)
            .apiTestCase(List.of(JobSceneCaseApi.builder().sceneCaseId(ID).build())).build();
        GrpcSceneCaseJobRequest grpcSceneCaseJobRequest = grpcMapper.toGrpcJobRequest(scheduleSceneCaseJobEntity);
        assertThat(grpcSceneCaseJobRequest.getId()).isEqualTo(scheduleSceneCaseJobEntity.getId());
        assertThat(grpcSceneCaseJobRequest.getEngineId()).isEqualTo(scheduleSceneCaseJobEntity.getEngineId());
        assertThat(grpcSceneCaseJobRequest.getWorkspaceId()).isEqualTo(scheduleSceneCaseJobEntity.getWorkspaceId());
        assertThat(grpcSceneCaseJobRequest.getProjectId()).isEqualTo(scheduleSceneCaseJobEntity.getProjectId());
        assertThat(grpcSceneCaseJobRequest.getEnvironment().getId())
            .isEqualTo(scheduleSceneCaseJobEntity.getEnvironment().getId());
        assertThat(grpcSceneCaseJobRequest.getDataCollection().getId())
            .isEqualTo(scheduleSceneCaseJobEntity.getDataCollection().getId());
        assertThat(grpcSceneCaseJobRequest.getApiTestCaseList().get(0).getSceneCaseId())
            .isEqualTo(scheduleSceneCaseJobEntity.getApiTestCase().get(0).getSceneCaseId());
    }

    @Test
    public void toGrpcJobRequest_ScheduleCaseJobEntity() {
        ScheduleCaseJobEntity scheduleCaseJobEntity = ScheduleCaseJobEntity
            .builder().id(ID).createDateTime(DATA).modifyDateTime(DATA).engineId(ID).workspaceId(ID).projectId(ID)
            .environment(jobEnv).dataCollection(jobDataCollection).apiTestCase(jobCaseApi).build();
        GrpcCaseJobRequest grpcCaseJobRequest = grpcMapper.toGrpcJobRequest(scheduleCaseJobEntity);
        assertThat(grpcCaseJobRequest.getId()).isEqualTo(scheduleCaseJobEntity.getId());
        assertThat(grpcCaseJobRequest.getEngineId()).isEqualTo(scheduleCaseJobEntity.getEngineId());
        assertThat(grpcCaseJobRequest.getWorkspaceId()).isEqualTo(scheduleCaseJobEntity.getWorkspaceId());
        assertThat(grpcCaseJobRequest.getProjectId()).isEqualTo(scheduleCaseJobEntity.getProjectId());
        assertThat(grpcCaseJobRequest.getEnvironment().getId())
            .isEqualTo(scheduleCaseJobEntity.getEnvironment().getId());
        assertThat(grpcCaseJobRequest.getDataCollection().getId())
            .isEqualTo(scheduleCaseJobEntity.getDataCollection().getId());
        assertThat(grpcCaseJobRequest.getApiTestCase().getJobApiTestCase().getId())
            .isEqualTo(scheduleCaseJobEntity.getApiTestCase().getJobApiTestCase().getId());
    }

    @Test
    public void toGrpcJobRequest_ApiTestCaseJobEntity() {
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity
            .builder().id(ID).createDateTime(DATA).modifyDateTime(DATA).createUserId(ID).modifyUserId(ID).engineId(ID)
            .workspaceId(ID).projectId(ID).environment(jobEnv).dataCollection(jobDataCollection).apiTestCase(jobCaseApi)
            .createUserName(NAME).build();
        GrpcCaseJobRequest grpcCaseJobRequest = grpcMapper.toGrpcJobRequest(apiTestCaseJobEntity);
        assertThat(grpcCaseJobRequest.getId()).isEqualTo(apiTestCaseJobEntity.getId());
        assertThat(grpcCaseJobRequest.getCreateUserId()).isEqualTo(apiTestCaseJobEntity.getCreateUserId());
        assertThat(grpcCaseJobRequest.getModifyUserId()).isEqualTo(apiTestCaseJobEntity.getModifyUserId());
        assertThat(grpcCaseJobRequest.getEngineId()).isEqualTo(apiTestCaseJobEntity.getEngineId());
        assertThat(grpcCaseJobRequest.getWorkspaceId()).isEqualTo(apiTestCaseJobEntity.getWorkspaceId());
        assertThat(grpcCaseJobRequest.getProjectId()).isEqualTo(apiTestCaseJobEntity.getProjectId());
        assertThat(grpcCaseJobRequest.getEnvironment().getId())
            .isEqualTo(apiTestCaseJobEntity.getEnvironment().getId());
        assertThat(grpcCaseJobRequest.getDataCollection().getId())
            .isEqualTo(apiTestCaseJobEntity.getDataCollection().getId());
        assertThat(grpcCaseJobRequest.getApiTestCase().getJobApiTestCase().getId())
            .isEqualTo(apiTestCaseJobEntity.getApiTestCase().getJobApiTestCase().getId());
        assertThat(grpcCaseJobRequest.getCreateUserName()).isEqualTo(apiTestCaseJobEntity.getCreateUserName());
    }

    @Test
    public void toGrpcJobRequest_ScheduleCaseJobEntity1() {
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity
            .builder().id(ID).createDateTime(DATA).modifyDateTime(DATA).createUserId(ID).modifyUserId(ID).engineId(ID)
            .workspaceId(ID).projectId(ID).environment(jobEnv).dataCollection(jobDataCollection).apiTestCase(jobCaseApi)
            .createUserName(NAME).build();
        GrpcCaseJobRequest grpcCaseJobRequest = grpcMapper.toGrpcJobRequest(apiTestCaseJobEntity);
        assertThat(grpcCaseJobRequest.getId()).isEqualTo(apiTestCaseJobEntity.getId());
        assertThat(grpcCaseJobRequest.getCreateUserId()).isEqualTo(apiTestCaseJobEntity.getCreateUserId());
        assertThat(grpcCaseJobRequest.getModifyUserId()).isEqualTo(apiTestCaseJobEntity.getModifyUserId());
        assertThat(grpcCaseJobRequest.getEngineId()).isEqualTo(apiTestCaseJobEntity.getEngineId());
        assertThat(grpcCaseJobRequest.getWorkspaceId()).isEqualTo(apiTestCaseJobEntity.getWorkspaceId());
        assertThat(grpcCaseJobRequest.getProjectId()).isEqualTo(apiTestCaseJobEntity.getProjectId());
        assertThat(grpcCaseJobRequest.getEnvironment().getId())
            .isEqualTo(apiTestCaseJobEntity.getEnvironment().getId());
        assertThat(grpcCaseJobRequest.getDataCollection().getId())
            .isEqualTo(apiTestCaseJobEntity.getDataCollection().getId());
        assertThat(grpcCaseJobRequest.getApiTestCase().getJobApiTestCase().getId())
            .isEqualTo(apiTestCaseJobEntity.getApiTestCase().getJobApiTestCase().getId());
        assertThat(grpcCaseJobRequest.getCreateUserName()).isEqualTo(apiTestCaseJobEntity.getCreateUserName());
    }

    @Test
    public void GrpcParamInfo_toGrpcParamInfo() {
        ParamInfo paramInfo = ParamInfo.builder().required(Boolean.TRUE).key(KEY).value(VALUE).paramType(PARAM_TYPE)
            .mockRule(MOCK_RULE).childParam(List.of(ParamInfo.builder().key(KEY).build())).build();
        GrpcParamInfo grpcParamInfo = grpcMapper.toGrpcParamInfo(paramInfo);
        assertThat(grpcParamInfo.getIsRequired()).isEqualTo(paramInfo.getRequired());
        assertThat(grpcParamInfo.getKey()).isEqualTo(paramInfo.getKey());
        assertThat(grpcParamInfo.getValue()).isEqualTo(paramInfo.getValue());
        assertThat(grpcParamInfo.getParamType()).isEqualTo(paramInfo.getParamType().getCode());
        assertThat(grpcParamInfo.getMockRule()).isEqualTo(paramInfo.getMockRule());
        assertThat(grpcParamInfo.getChildParamList().get(0).getKey())
            .isEqualTo(paramInfo.getChildParam().get(0).getKey());
    }

    @Test
    public void GrpcParamInfo_toGrpcMatchParamInfo() {
        MatchParamInfo matchParamInfo = MatchParamInfo.builder()
            .key(KEY).value(VALUE).matchType(MATCH_TYPE).paramType(PARAM_TYPE)
            .childParam(List.of(MatchParamInfo.builder().key(KEY).build())).build();
        GrpcMatchParamInfo grpcMatchParamInfo = grpcMapper.toGrpcMatchParamInfo(matchParamInfo);
        assertThat(grpcMatchParamInfo.getKey()).isEqualTo(matchParamInfo.getKey());
        assertThat(grpcMatchParamInfo.getValue()).isEqualTo(matchParamInfo.getValue());
        assertThat(grpcMatchParamInfo.getMatchType()).isEqualTo(matchParamInfo.getMatchType().getCode());
        assertThat(grpcMatchParamInfo.getParamType()).isEqualTo(matchParamInfo.getParamType().getCode());
        assertThat(grpcMatchParamInfo.getChildParam(0).getKey())
            .isEqualTo(matchParamInfo.getChildParam().get(0).getKey());
    }

    @Test
    public void GrpcJobSceneCaseApi_JobSceneCaseApi() {
        JobSceneCaseApi jobSceneCaseApi = JobSceneCaseApi.builder()
            .caseId(ID).id(ID).sceneCaseId(ID).caseTemplateId(ID).apiType(ApiType.getApiType(1)).shell(SHELL)
            .order(ORDER).jobApiTestCase(JobApiTestCase.builder().id(ID).build()).build();
        GrpcJobSceneCaseApi grpcJobSceneCaseApi = grpcMapper.toGrpcJobSceneCaseApi(jobSceneCaseApi);
        assertThat(jobSceneCaseApi.getCaseId()).isEqualTo(grpcJobSceneCaseApi.getCaseId());
        assertThat(jobSceneCaseApi.getId()).isEqualTo(grpcJobSceneCaseApi.getId());
        assertThat(jobSceneCaseApi.getSceneCaseId()).isEqualTo(grpcJobSceneCaseApi.getSceneCaseId());
        assertThat(jobSceneCaseApi.getCaseTemplateId()).isEqualTo(grpcJobSceneCaseApi.getCaseTemplateId());
        assertThat(jobSceneCaseApi.getApiType().getCode()).isEqualTo(grpcJobSceneCaseApi.getApiType());
        assertThat(jobSceneCaseApi.getShell()).isEqualTo(grpcJobSceneCaseApi.getShell());
        assertThat(jobSceneCaseApi.getOrder()).isEqualTo(grpcJobSceneCaseApi.getOrder());
        assertThat(jobSceneCaseApi.getJobApiTestCase().getId())
            .isEqualTo(grpcJobSceneCaseApi.getJobApiTestCase().getId());
    }

    @Test
    public void GrpcJobSceneCaseApi_JobApiTestCase() {
        String StatusCode = "StatusCode";
        HttpStatusVerification httpStatusVerification = new HttpStatusVerification();
        httpStatusVerification.setStatusCode(StatusCode);
        ResponseHeadersVerification responseHeadersVerification = new ResponseHeadersVerification();
        responseHeadersVerification.setParams(List.of(MatchParamInfo.builder().key(KEY).build()));
        ResponseResultVerification responseResultVerification = new ResponseResultVerification();
        responseResultVerification.setParams(List.of(MatchParamInfo.builder().key(KEY).build()));
        ResponseTimeVerification responseTimeVerification = new ResponseTimeVerification();
        responseTimeVerification.setTimeoutLimit(Duration.ofDays(1));

        JobApiTestCase jobApiTestCase = JobApiTestCase.builder()
            .id(ID).caseName(NAME).responseParamsExtractionType(ResponseParamsExtractionType.getType(1))
            .httpStatusVerification(httpStatusVerification).responseHeadersVerification(responseHeadersVerification)
            .responseResultVerification(responseResultVerification).responseTimeVerification(responseTimeVerification)
            .dataCollId(ID).advancedSetting(new AdvancedSetting(Duration.ofMillis(10), true))
            .jobApi(JobApi.builder().apiName(NAME).build()).build();

        GrpcJobApiTestCase grpcJobApiTestCase = grpcMapper.toGrpcJobApiTestCase(jobApiTestCase);

        assertThat(jobApiTestCase.getId()).isEqualTo(grpcJobApiTestCase.getId());
        assertThat(jobApiTestCase.getCaseName()).isEqualTo(grpcJobApiTestCase.getCaseName());
        assertThat(jobApiTestCase.getResponseParamsExtractionType().getCode())
            .isEqualTo(grpcJobApiTestCase.getResponseParamsExtractionType());
        assertThat(jobApiTestCase.getHttpStatusVerification().getStatusCode())
            .isEqualTo(grpcJobApiTestCase.getHttpStatusVerification().getStatusCode());
        assertThat(jobApiTestCase.getResponseHeadersVerification().getParams().get(0).getKey())
            .isEqualTo(grpcJobApiTestCase.getResponseHeadersVerification().getParams(0).getKey());
        assertThat(jobApiTestCase.getResponseResultVerification().getParams().get(0).getKey())
            .isEqualTo(grpcJobApiTestCase.getResponseResultVerification().getParams(0).getKey());
        assertThat(jobApiTestCase.getDataCollId()).isEqualTo(grpcJobApiTestCase.getDataCollId());
        assertThat(jobApiTestCase.getJobApi().getApiName()).isEqualTo(grpcJobApiTestCase.getJobApi().getApiName());
    }

    @Test
    public void GrpcJobSceneCaseApi_toGrpcJobApiResponse() {
        String description = "description";
        String apiPath = "apiPath";
        String responseRaw = "responseRaw";
        JobApi jobApi = JobApi.builder()
            .projectId(ID).apiName(NAME).description(description).apiPath(apiPath).apiProtocol(ApiProtocol.getType(1))
            .requestMethod(RequestMethod.getType(1)).apiRequestParamType(ApiRequestParamType.getType(1))
            .requestRaw(responseRaw).requestRawType(RawType.getType(1)).preInject(INJECT).postInject(INJECT)
            .apiRequestJsonType(ApiJsonType.getType(1)).apiResponseJsonType(ApiJsonType.getType(1))
            .apiResponseParamType(ApiRequestParamType.getType(1)).responseRaw(responseRaw)
            .responseRawType(RawType.getType(1)).requestHeaders(List.of(ParamInfo.builder().key(KEY).build()))
            .responseHeaders(List.of(ParamInfo.builder().key(KEY).build()))
            .pathParams(List.of(ParamInfo.builder().key(KEY).build()))
            .restfulParams(List.of(ParamInfo.builder().key(KEY).build()))
            .requestParams(List.of(ParamInfo.builder().key(KEY).build()))
            .responseParams(List.of(ParamInfo.builder().key(KEY).build())).build();
        GrpcJobApi grpcJobApi = grpcMapper.toGrpcJobApiResponse(jobApi);

        assertThat(jobApi.getProjectId()).isEqualTo(grpcJobApi.getProjectId());
        assertThat(jobApi.getApiName()).isEqualTo(grpcJobApi.getApiName());
        assertThat(jobApi.getDescription()).isEqualTo(grpcJobApi.getDescription());
        assertThat(jobApi.getApiPath()).isEqualTo(grpcJobApi.getApiPath());
        assertThat(jobApi.getApiProtocol().getCode()).isEqualTo(grpcJobApi.getApiProtocol());
        assertThat(jobApi.getRequestMethod().getCode()).isEqualTo(grpcJobApi.getRequestMethod());
        assertThat(jobApi.getApiRequestParamType().getCode()).isEqualTo(grpcJobApi.getApiRequestParamType());
        assertThat(jobApi.getRequestRaw()).isEqualTo(grpcJobApi.getRequestRaw());
        assertThat(jobApi.getRequestRawType().getCode()).isEqualTo(grpcJobApi.getRequestRawType());
        assertThat(jobApi.getPreInject()).isEqualTo(grpcJobApi.getPreInject());
        assertThat(jobApi.getPostInject()).isEqualTo(grpcJobApi.getPostInject());
        assertThat(jobApi.getApiResponseJsonType().getCode()).isEqualTo(grpcJobApi.getApiResponseJsonType());
        assertThat(jobApi.getApiRequestJsonType().getCode()).isEqualTo(grpcJobApi.getApiRequestJsonType());
        assertThat(jobApi.getApiResponseParamType().getCode()).isEqualTo(grpcJobApi.getApiResponseParamType());
        assertThat(jobApi.getResponseRaw()).isEqualTo(grpcJobApi.getResponseRaw());
        assertThat(jobApi.getRequestHeaders().get(0).getKey()).isEqualTo(grpcJobApi.getRequestHeaders(0).getKey());
        assertThat(jobApi.getResponseHeaders().get(0).getKey()).isEqualTo(grpcJobApi.getResponseHeaders(0).getKey());
        assertThat(jobApi.getRestfulParams().get(0).getKey()).isEqualTo(grpcJobApi.getRestfulParams(0).getKey());
        assertThat(jobApi.getRequestParams().get(0).getKey()).isEqualTo(grpcJobApi.getRequestParams(0).getKey());
        assertThat(jobApi.getRequestParams().get(0).getKey()).isEqualTo(grpcJobApi.getRequestParams(0).getKey());
        assertThat(jobApi.getResponseParams().get(0).getKey()).isEqualTo(grpcJobApi.getResponseParams(0).getKey());
    }

    @Test
    public void GrpcResponseResultVerification_toGrpcResponseResultVerification() {
        ResponseResultVerification responseResultVerification = new ResponseResultVerification();
        responseResultVerification.setResultVerificationType(ResultVerificationType.getType(1));
        responseResultVerification.setApiResponseJsonType(ApiJsonType.getType(1));
        responseResultVerification.setVerificationElementType(VerificationElementType.getType(1));

        GrpcResponseResultVerification grpcResponseResultVerification = grpcMapper
            .toGrpcResponseResultVerification(responseResultVerification);

        assertThat(responseResultVerification.getResultVerificationType().getCode())
            .isEqualTo(grpcResponseResultVerification.getResultVerificationType());
        assertThat(responseResultVerification.getApiResponseJsonType().getCode())
            .isEqualTo(grpcResponseResultVerification.getApiResponseJsonType());
        assertThat(responseResultVerification.getVerificationElementType().getCode())
            .isEqualTo(grpcResponseResultVerification.getVerificationElementType());
    }

    @Test
    public void SceneCaseJobReport_toJobReport() {
        GrpcSceneCaseJobReport grpcSceneCaseJobReport = GrpcSceneCaseJobReport.newBuilder()
            .setJobId(ID).setMessage(MESSAGE).setErrCode(CODE).setDelayTimeTotalTimeCost(1)
            .setJobType(JobType.SCHEDULE_CATE
                .name()).setDelayTimeTotalTimeCost(1).addCaseReportList(GrpcCaseReport.newBuilder().addInfoList(VALUE))
            .build();
        SceneCaseJobReport sceneCaseJobReport = grpcMapper.toJobReport(grpcSceneCaseJobReport);
        assertThat(grpcSceneCaseJobReport.getJobId()).isEqualTo(sceneCaseJobReport.getJobId());
        assertThat(grpcSceneCaseJobReport.getCaseReportListList().get(0).getInfoList(0))
            .isEqualTo(grpcSceneCaseJobReport.getCaseReportListList().get(0).getInfoList(0));
    }

    @Test
    public void ApiTestCaseJobReport_toJobReport() {
        GrpcCaseJobReport grpcCaseJobReport = GrpcCaseJobReport.newBuilder()
            .setJobId(ID).setMessage(MESSAGE).setErrCode(CODE).setJobType(JobType.CASE.name())
            .setCaseReport(GrpcCaseReport.newBuilder().setCaseId(ID).addInfoList(VALUE)).build();
        ApiTestCaseJobReport apiTestCaseJobReport = grpcMapper.toJobReport(grpcCaseJobReport);
        assertThat(grpcCaseJobReport.getJobId()).isEqualTo(apiTestCaseJobReport.getJobId());
        assertThat(grpcCaseJobReport.getCaseReport().getCaseId())
            .isEqualTo(apiTestCaseJobReport.getCaseReport().getCaseId());
        assertThat(grpcCaseJobReport.getCaseReport().getInfoList(0))
            .isEqualTo(apiTestCaseJobReport.getCaseReport().getInfoList().get(0));
    }

    @Test
    public void GrpcFunction_projectFunction() {
        ProjectFunctionEntity projectFunction = ProjectFunctionEntity.builder()
            .id(ID).projectId(ID).functionKey(KEY).functionName(NAME).functionCode(CODE)
            .functionParams(List.of(ParamInfo.builder().key(KEY).build())).build();
        GrpcFunction grpcFunction = grpcMapper.toGrpcFunction(projectFunction);
        assertThat(projectFunction.getId()).isEqualTo(grpcFunction.getId());
        assertThat(projectFunction.getProjectId()).isEqualTo(grpcFunction.getProjectId());
        assertThat(projectFunction.getFunctionName()).isEqualTo(grpcFunction.getFunctionName());
        assertThat(projectFunction.getFunctionCode()).isEqualTo(grpcFunction.getFunctionCode());
        assertThat(projectFunction.getFunctionParams().get(0).getKey())
            .isEqualTo(grpcFunction.getFunctionParams(0).getKey());
    }

    @Test
    public void GrpcFunction_toGrpcFunction() {
        GlobalFunctionEntity globalFunction = GlobalFunctionEntity.builder().id(ID).workspaceId(ID).functionKey(KEY)
            .functionName(NAME).functionCode(CODE).functionParams(List.of(ParamInfo.builder().key(KEY).build()))
            .build();
        GrpcFunction grpcFunction = grpcMapper.toGrpcFunction(globalFunction);
        assertThat(globalFunction.getId()).isEqualTo(grpcFunction.getId());
        assertThat(globalFunction.getWorkspaceId()).isEqualTo(grpcFunction.getWorkspaceId());
        assertThat(globalFunction.getFunctionName()).isEqualTo(grpcFunction.getFunctionName());
        assertThat(globalFunction.getFunctionCode()).isEqualTo(grpcFunction.getFunctionCode());
        assertThat(globalFunction.getFunctionParams().get(0).getKey())
            .isEqualTo(grpcFunction.getFunctionParams(0).getKey());
    }
}
