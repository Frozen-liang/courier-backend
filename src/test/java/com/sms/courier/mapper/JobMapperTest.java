package com.sms.courier.mapper;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.DataParamRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.dto.response.*;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.EnvironmentAuth;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.*;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for JobMapper")
class JobMapperTest {

    private ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));
    private static final String NAME = "testName";
    private static final LocalDateTime MODIFY_TIME=LocalDateTime.now();
    private static final LocalDateTime CREATE_TIME=LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the projectEnvironment object to a jobEnvironment object")
    void projectEnvironment_to_jobEnvironment() {
        EnvironmentAuth auth = new EnvironmentAuth();
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder()
                .projectId("1")
                .envAuth(auth)
                .envName(NAME)
                .headers(Lists.newArrayList())
                .envVariable(Lists.newArrayList())
                .urlParams(Lists.newArrayList())
                .requestParams(Lists.newArrayList())
                .build();
        JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
        assertThat(jobEnvironment.getEnvName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseResponse object to a jobApiTestCase object")
    void apiTestCaseResponse_to_jobApiTestCase() {
        ApiTestCaseEntity apiTestCaseResponse =
            ApiTestCaseEntity.builder().apiEntity(ApiEntity.builder().apiName(NAME).build()).build();
        List<MatchParamInfoResponse> list = Lists.newArrayList();
        ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
                .modifyDateTime(MODIFY_TIME)
                .responseHeadersVerification(ResponseHeadersVerificationResponse.builder()
                        .params(list)
                        .build())
                .apiEntity(ApiResponse.builder()
                        .apiName(NAME)
                        .requestHeaders(Lists.newArrayList(ParamInfoResponse.builder().paramType(0).build()))
                        .build())
                .build();
        JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestCaseResponse);
        assertThat(jobApiTestCase.getJobApi().getApiName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseResponse object to a jobApiTestCase object")
    void toJobApiTestCase_isNull_Test(){
        ApiTestCaseResponse apiTestCaseResponse =null;
        JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestCaseResponse);
        assertThat(jobApiTestCase).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseResponse object to a jobApiTestCase object")
    void apiTestCaseResponse_to_jobApiTestCase_isNull() {
        ApiTestRequest apiTestRequest= ApiTestRequest.builder()
                .build();
        JobApiTestCase dto=jobMapper.toJobApiTestCase(apiTestRequest);
        assertThat(Lists.newArrayList(dto)).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test the method to convert the dataCollectionRequest object to a jobDataCollection object")
    void dataCollectionRequest_to_jobDataCollection() {
        DataCollectionRequest dataCollectionRequest = DataCollectionRequest.builder().collectionName(NAME).build();
        JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
        assertThat(jobDataCollection.getCollectionName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the testDataRequest object to a testData object")
    void testDataRequest_to_testData() {
        TestDataRequest testDataRequest = TestDataRequest.builder().dataName(NAME).build();
        TestData testData = jobMapper.toTestDataEntity(testDataRequest);
        assertThat(testData.getDataName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobResponse object")
    void apiTestCaseJob_to_apiTestCaseJobResponse() {
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder()

                .jobStatus(JobStatus.SUCCESS).build();
        ApiTestCaseJobResponse apiTestCaseJobResponse = jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(apiTestCaseJobResponse.getJobStatus()).isEqualTo(JobStatus.SUCCESS.getCode());
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void apiTestCaseJob_to_apiTestCaseJobPageResponse() {
        String message = "success";
        String requestUrl = "localhost";
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder().message(message)
            .apiTestCase(JobCaseApi.builder().jobApiTestCase(
                JobApiTestCase.builder()
                        .tagId(Lists.newArrayList())
                        .caseReport(CaseReport.builder()
                                .requestUrl(requestUrl).build()).build())
                .build()).build();
        ApiTestCaseJobResponse apiTestCaseJobResponse = jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(apiTestCaseJobResponse.getMessage()).isEqualTo(message);
        assertThat(apiTestCaseJobResponse.getApiTestCase().getJobApiTestCase().getCaseReport().getRequestUrl())
            .isEqualTo(requestUrl);
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
        void toJobDataCollectionIsNull_Test(){
            assertThat(jobMapper.toJobDataCollection(null)).isNull();
        }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toTestDataEntityIsNull_Test(){
        assertThat(jobMapper.toApiTestCaseJobResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobApiTestCaseIsNull_Test(){
        ApiTestRequest apiTestRequest=null;
        JobApiTestCase dot=jobMapper.toJobApiTestCase(apiTestRequest);
        assertThat(dot).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toApiTestCaseJobPageResponseIsNull_Test(){
        assertThat(jobMapper.toApiTestCaseJobPageResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiList_Test(){
        SceneCaseApiEntity sceneCaseApiEntity=SceneCaseApiEntity.builder().build();
        List<SceneCaseApiEntity> sceneCaseApiEntities=Lists.newArrayList(sceneCaseApiEntity);
        List<JobSceneCaseApi> dto=jobMapper.toJobSceneCaseApiList(sceneCaseApiEntities);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiListIsNull_Test(){
        List<SceneCaseApiEntity> sceneCaseApiEntities=null;
        List<JobSceneCaseApi> dto=jobMapper.toJobSceneCaseApiList(sceneCaseApiEntities);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiIsNull_Test(){
        assertThat(jobMapper.toJobSceneCaseApi(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiListByTemplate_Test(){
        CaseTemplateApiEntity caseTemplateApiEntity=CaseTemplateApiEntity.builder().build();
        List<CaseTemplateApiEntity> caseTemplateApiList=Lists.newArrayList(caseTemplateApiEntity);
        List<JobSceneCaseApi> dto=jobMapper.toJobSceneCaseApiListByTemplate(Lists.newArrayList(caseTemplateApiList));
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiListByTemplate_IsNull_Test(){
        assertThat(jobMapper.toJobSceneCaseApiListByTemplate(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toJobSceneCaseApiByTemplate_IsNull_Test(){
        assertThat(jobMapper.toJobSceneCaseApiByTemplate(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toSceneCaseJobResponse_Test(){
        SceneCaseJobEntity sceneCaseJob=SceneCaseJobEntity.builder()
                .modifyDateTime(MODIFY_TIME)
                .createDateTime(CREATE_TIME)
                .build();
        SceneCaseJobResponse dot=jobMapper.toSceneCaseJobResponse(sceneCaseJob);
        assertThat(dot).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toSceneCaseJobResponse_isNull_Test(){
        SceneCaseJobResponse dot=jobMapper.toSceneCaseJobResponse(null);
        assertThat(dot).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toSceneCaseJobResponseIsNull_Test(){

        assertThat(jobMapper.toApiTestCaseJobReportResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toSceneCaseJobReportResponse_Test(){
        SceneCaseJobReport caseJobReport=SceneCaseJobReport.builder().build();
        SceneCaseJobReportResponse dot=jobMapper.toSceneCaseJobReportResponse(caseJobReport);
        assertThat(dot).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toSceneCaseJobReportResponse_IsNull_Test(){
        assertThat(jobMapper.toSceneCaseJobReportResponse(null)).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toTestDataEntity_Test(){
        TestData dto = jobMapper.toTestDataEntity(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void toApiTestCaseJobPageResponse_Test(){
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder()
                .createDateTime(CREATE_TIME)
                .build();
        ApiTestCaseJobPageResponse dto = jobMapper.toApiTestCaseJobPageResponse(apiTestCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void dataParamRequestToDataParam_IsNull_Test(){
        DataParamRequest dataParamRequest = null;
        TestDataRequest testDataRequest = TestDataRequest.builder()
                .data(Lists.newArrayList(dataParamRequest))
                .build();
        TestData dto = jobMapper.toTestDataEntity(testDataRequest);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void matchParamInfoResponseListToMatchParamInfoList_IsNull_Test(){
        List<MatchParamInfoResponse> list = null;
        ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
                .responseHeadersVerification(new ResponseHeadersVerificationResponse(list))
                .build();
        JobApiTestCase dto = jobMapper.toJobApiTestCase(apiTestCaseResponse);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void matchParamInfoResponseListToMatchParamInfoList_Test(){
        List<MatchParamInfoResponse> list = Lists.newArrayList(MatchParamInfoResponse.builder()
                .matchType(0)
                .paramType(0)
                .build());
        ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
                .responseHeadersVerification(new ResponseHeadersVerificationResponse(list))
                .build();
        JobApiTestCase dto = jobMapper.toJobApiTestCase(apiTestCaseResponse);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void httpStatusVerificationToHttpStatusVerificationResponse_Test(){
        HttpStatusVerification httpStatusVerification = HttpStatusVerification.builder().build();
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder()
                .apiTestCase(new JobCaseApi(JobApiTestCase.builder()
                        .httpStatusVerification(httpStatusVerification)
                        .build()))
                .build();
        ApiTestCaseJobResponse dto =jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void matchParamInfoListToMatchParamInfoResponseList_Test(){
        List<MatchParamInfo> list = Lists.newArrayList(MatchParamInfo.builder().build());
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder()
                .apiTestCase(new JobCaseApi(JobApiTestCase.builder()
                        .responseHeadersVerification(ResponseHeadersVerification.builder()
                                .params(list)
                                .build())
                        .build()))
                .build();
        ApiTestCaseJobResponse dto = jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void responseTimeVerificationToResponseTimeVerificationResponse_Test(){
        ResponseTimeVerification responseTimeVerification=ResponseTimeVerification.builder().build();
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder()
                .apiTestCase(new JobCaseApi(JobApiTestCase.builder()
                        .responseTimeVerification(responseTimeVerification)
                        .build()))
                .build();
        ApiTestCaseJobResponse dto = jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void apiTestCaseJobApiTestCaseJobApiTestCaseCaseReport_Test(){
        Map<String, Object> map = new HashMap<>();
        map.put("123","123");
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity.builder()
                .apiTestCase(JobCaseApi.builder()
                        .jobApiTestCase(JobApiTestCase.builder()
                                .caseReport(CaseReport.builder().requestHeader(map).responseHeader(map).build())
                                .build()).build())
                .build();
        ApiTestCaseJobPageResponse dto = jobMapper.toApiTestCaseJobPageResponse(apiTestCaseJobEntity);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void apiTestCaseJobApiTestCaseJobApiTestCaseCaseReport__Test(){
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity.builder()
                .apiTestCase(JobCaseApi.builder()
                        .jobApiTestCase(null).build())
                .build();
        ApiTestCaseJobPageResponse dto = jobMapper.toApiTestCaseJobPageResponse(apiTestCaseJobEntity);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void apiTestCaseEntityToJobApiTestCase_Test(){
        ApiTestCaseEntity apiTestCaseEntity = ApiTestCaseEntity.builder()
                .tagId(Lists.newArrayList())
                .apiEntity(ApiEntity.builder()
                        .requestHeaders(Lists.newArrayList())
                        .pathParams(Lists.newArrayList())
                        .requestParams(Lists.newArrayList())
                        .restfulParams(Lists.newArrayList())
                        .responseParams(Lists.newArrayList())
                        .build())
                .modifyDateTime(MODIFY_TIME)
                .build();
        CaseTemplateApiEntity caseTemplateApiList = CaseTemplateApiEntity.builder()
                .apiTestCase(apiTestCaseEntity)
                .build();
        JobSceneCaseApi dto = jobMapper.toJobSceneCaseApiByTemplate(caseTemplateApiList);
        assertThat(dto).isNotNull();

    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void jobSceneCaseApiListToJobSceneCaseApiResponseList_Test(){
        List<JobSceneCaseApi> list = Lists.newArrayList(JobSceneCaseApi.builder().build());
        SceneCaseJobEntity sceneCaseJob = SceneCaseJobEntity.builder()
                .apiTestCase(list)
                .build();
        SceneCaseJobResponse dto = jobMapper.toSceneCaseJobResponse(sceneCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void jobSceneCaseApiListToJobSceneCaseApiResponseList_IsNull_Test(){
        JobSceneCaseApi jobSceneCaseApi = null;
        List<JobSceneCaseApi> list = Lists.newArrayList(jobSceneCaseApi);
        SceneCaseJobEntity sceneCaseJob = SceneCaseJobEntity.builder()
                .apiTestCase(list)
                .build();
        SceneCaseJobResponse dto = jobMapper.toSceneCaseJobResponse(sceneCaseJob);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseJob object to a apiTestCaseJobPageResponse object")
    void caseReportListToCaseReportResponseList_Test(){
        List<CaseReport> list = Lists.newArrayList(CaseReport.builder().build());
        SceneCaseJobReport caseJobReport = SceneCaseJobReport.builder()
                .caseReportList(list)
                .build();
        SceneCaseJobReportResponse dto = jobMapper.toSceneCaseJobReportResponse(caseJobReport);
        assertThat(dto).isNotNull();

    }


}