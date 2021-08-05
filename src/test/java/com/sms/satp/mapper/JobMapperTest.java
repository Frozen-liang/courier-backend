package com.sms.satp.mapper;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import com.sms.satp.entity.job.JobCaseApi;
import com.sms.satp.entity.job.common.CaseReport;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for JobMapper")
class JobMapperTest {

    private ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));
    private static final String NAME = "testName";

    @Test
    @DisplayName("Test the method to convert the projectEnvironment object to a jobEnvironment object")
    void projectEnvironment_to_jobEnvironment() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().envName(NAME).build();
        JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
        assertThat(jobEnvironment.getEnvName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the apiTestCaseResponse object to a jobApiTestCase object")
    void apiTestCaseResponse_to_jobApiTestCase() {
        ApiTestCaseResponse apiTestCaseResponse =
            ApiTestCaseResponse.builder().apiEntity(ApiResponse.builder().apiName(NAME).build()).build();
        JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestCaseResponse);
        assertThat(jobApiTestCase.getApiEntity().getApiName()).isEqualTo(NAME);
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
        ApiTestCaseJobEntity apiTestCaseJob = ApiTestCaseJobEntity.builder().jobStatus(JobStatus.SUCCESS).build();
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
                JobApiTestCase.builder().caseReport(CaseReport.builder().requestUrl(requestUrl).build()).build())
                .build()).build();
        ApiTestCaseJobResponse apiTestCaseJobResponse = jobMapper.toApiTestCaseJobResponse(apiTestCaseJob);
        assertThat(apiTestCaseJobResponse.getMessage()).isEqualTo(message);
        assertThat(apiTestCaseJobResponse.getApiTestCase().getJobApiTestCase().getCaseReport().getRequestUrl())
            .isEqualTo(requestUrl);
    }

}