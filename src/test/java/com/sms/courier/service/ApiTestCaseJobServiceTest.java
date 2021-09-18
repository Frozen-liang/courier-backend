package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.BUILD_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_ENV_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.DataParamRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.mapper.JobMapperImpl;
import com.sms.courier.mapper.MatchParamInfoMapperImpl;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.mapper.ParamInfoMapperImpl;
import com.sms.courier.mapper.ResponseResultVerificationMapperImpl;
import com.sms.courier.repository.ApiTestCaseJobRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiTestCaseJobRepository;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.impl.ApiTestCaseJobServiceImpl;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;

@DisplayName("Tests for ApiTestCaseJobService")
class ApiTestCaseJobServiceTest {

    private final ApiTestCaseJobRepository apiTestCaseJobRepository = mock(ApiTestCaseJobRepository.class);
    private final CaseDispatcherService caseDispatcherService = mock(CaseDispatcherService.class);
    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final ApiTestCaseService apiTestCaseService = mock(ApiTestCaseService.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository = mock(
        CustomizedApiTestCaseJobRepository.class);
    private final ApiTestRequest apiTestRequest =
        ApiTestRequest.builder().envId(ObjectId.get().toString()).apiPath("3Httt").build();
    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));
    private final ApiTestCaseJobService apiTestCaseJobService = new ApiTestCaseJobServiceImpl(
        apiTestCaseJobRepository, customizedApiTestCaseJobRepository, caseDispatcherService, projectEnvironmentService
        , apiTestCaseService, commonRepository, jobMapper);
    private final ApiTestCaseJobEntity apiTestCaseJob =
        ApiTestCaseJobEntity.builder().id(ID).createUserId(ObjectId.get().toString())
            .apiTestCase(JobCaseApi.builder().jobApiTestCase(JobApiTestCase.builder().build()).build()).build();
    private final TestDataRequest testDataRequest =
        TestDataRequest.builder().dataName("test")
            .data(List.of(DataParamRequest.builder().key("key").value("value").build())).build();
    private final ApiTestCaseJobRunRequest apiTestCaseJobRunRequest =
        ApiTestCaseJobRunRequest.builder().apiTestCaseIds(Collections.singletonList(ObjectId.get().toString()))
            .envId(ObjectId.get().toString())
            .dataCollectionRequest(
                DataCollectionRequest.builder().collectionName("test")
                    .dataList(Collections.singletonList(testDataRequest)).build()).build();
    private final ApiTestCaseJobRunRequest apiTestCaseJobRunRequest2 =
        ApiTestCaseJobRunRequest.builder().apiTestCaseIds(Collections.singletonList(ObjectId.get().toString()))
            .envId(ObjectId.get().toString())
            .build();
    private final ApiTestCaseEntity apiTestCaseEntity = ApiTestCaseEntity.builder().id(ID).build();
    private final ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().build();
    private static final String ID = ObjectId.get().toString();
    private static final String ENGINE_ID = "/engine/13/invoke";
    private static final List<String> ENGINE_ID_LIST = Collections.singletonList(ENGINE_ID);
    private final CustomUser customUser =
        new CustomUser("username", "", Collections.emptyList(), ObjectId.get().toString(), "", "nickname",
            TokenType.USER,
            LocalDate.now());
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the findById method in the ApiTestCaseJob service")
    public void findById_test() {
        when(apiTestCaseJobRepository.findById(ID)).thenReturn(Optional.of(apiTestCaseJob));
        ApiTestCaseJobResponse apiTestCaseJobResponse = apiTestCaseJobService.get(ID);
        assertThat(apiTestCaseJobResponse).isNotNull();
        assertThat(apiTestCaseJobResponse.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCaseJob")
    public void findById_exception_test() {
        when(apiTestCaseJobRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiTestCaseJobService.get(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_JOB_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the page method in the ApiTestCaseJob service")
    public void page_test() {
        when(customizedApiTestCaseJobRepository.page(any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertThat(apiTestCaseJobService.page(new ApiTestCaseJobPageRequest())).isNotNull();
    }

    @Test
    @DisplayName("Test the handleJobReport method in the ApiTestCaseJob service")
    public void handleJobReport_test() {
        when(apiTestCaseJobRepository.findById(any())).thenReturn(Optional.of(apiTestCaseJob));
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        doNothing().when(caseDispatcherService).sendJobReport(anyString(), any(ApiTestCaseJobReportResponse.class));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        doNothing().when(apiTestCaseService).insertTestResult(anyString(), any());
        apiTestCaseJobService.handleJobReport(ApiTestCaseJobReport.builder().jobId(ObjectId.get().toString()).build());
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
    }

    @Test
    @DisplayName("Test the runJob method in the ApiTestCaseJob service")
    public void runJob_test() {
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest, customUser);
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
    }

    @Test
    @DisplayName("Test the runJob method in the ApiTestCaseJob service")
    public void runJob2_test() {
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest2, customUser);
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
    }

    @Test
    @DisplayName("An exception occurred while execute ApiTestCaseJob")
    public void environment_not_exist_exception_test() {
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest, customUser);
        doNothing().when(caseDispatcherService).sendCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("An exception occurred while execute ApiTestCaseJob")
    public void execute_exception_test() {
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(projectEnvironmentService.findOne(any())).thenThrow(new RuntimeException());
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest, customUser);
        doNothing().when(caseDispatcherService).sendJobReport(anyString(), any(ApiTestCaseJobReportResponse.class));
        doNothing().when(caseDispatcherService).sendCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("Test the apiTest method in the ApiTestCaseJob service")
    public void apiTest2_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        apiTestCaseJobService.apiTest(apiTestRequest, customUser);
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
    }

    @Test
    @DisplayName("An exception occurred while apiTest ApiTestCaseJob")
    public void apiTest_exception1_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        apiTestCaseJobService.apiTest(apiTestRequest, customUser);
        doNothing().when(caseDispatcherService).sendCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());

    }

    @Test
    @DisplayName("An exception occurred while apiTest ApiTestCaseJob")
    public void apiTest_exception2_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        apiTestCaseJobService.apiTest(null, customUser);
        doNothing().when(caseDispatcherService).sendCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());

    }

    @Test
    @DisplayName("Test the reallocateJob method in the ApiTestCaseJob service")
    public void reallocateJob_test() {
        when(apiTestCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(apiTestCaseJob));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        apiTestCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
    }

    @Test
    @DisplayName("An apiTestPlatformException occurred while run reallocateJob in ApiTestCaseJob service")
    public void reallocateJob_ApiTestPlatformException_test() {
        when(apiTestCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(apiTestCaseJob));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class)))
            .thenThrow(ExceptionUtils.mpe(ErrorCode.EXECUTE_API_TEST_CASE_ERROR));
        apiTestCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("An exception occurred while run reallocateJob in ApiTestCaseJob service")
    public void reallocateJob_Exception_test() {
        when(apiTestCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(apiTestCaseJob));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class)))
            .thenThrow(new RuntimeException());
        apiTestCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(caseDispatcherService, times(1)).sendCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("Test the buildJob method in the ApiTestCaseJob service")
    public void buildJob_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        ApiTestCaseJobResponse apiTestCaseJobResponse = apiTestCaseJobService.buildJob(apiTestRequest);
        assertThat(apiTestCaseJobResponse).isNotNull();
    }

    @Test
    @DisplayName("An custom courier exception occurred while running buildJob")
    public void buildJob_custom_courier_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        assertThatThrownBy(() -> apiTestCaseJobService.buildJob(apiTestRequest)).extracting("code")
            .isEqualTo(THE_ENV_NOT_EXIST.getCode());
    }

    @Test
    @DisplayName("An runtime exception occurred while running buildJob")
    public void buildJob_runtime_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenThrow(new RuntimeException());
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        assertThatThrownBy(() -> apiTestCaseJobService.buildJob(apiTestRequest)).extracting("code")
            .isEqualTo(BUILD_CASE_JOB_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the buildJobs method in the ApiTestCaseJob service")
    public void buildJobs_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        List<ApiTestCaseJobResponse> apiTestCaseJobResponses = apiTestCaseJobService.buildJob(apiTestCaseJobRunRequest);
        assertThat(apiTestCaseJobResponses).isNotEmpty();
    }

    @Test
    @DisplayName("An custom courier exception occurred while running buildJobs")
    public void buildJobs_custom_courier_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        when(apiTestCaseService.findOne(any())).thenReturn(apiTestCaseEntity);
        when(apiTestCaseJobRepository.save(any())).thenReturn(apiTestCaseEntity);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        assertThatThrownBy(() -> apiTestCaseJobService.buildJob(apiTestCaseJobRunRequest)).extracting("code")
            .isEqualTo(THE_ENV_NOT_EXIST.getCode());
    }

    @Test
    @DisplayName("An runtime exception occurred while running buildJobs")
    public void buildJobs_runtime_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(projectEnvironment);
        when(apiTestCaseJobRepository.save(any())).thenThrow(new RuntimeException());
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        assertThatThrownBy(() -> apiTestCaseJobService.buildJob(apiTestCaseJobRunRequest2)).extracting("code")
            .isEqualTo(BUILD_CASE_JOB_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the insertJobReport method in the ApiTestCaseJob service")
    public void insertJobReport_test() {
        when(apiTestCaseJobRepository.findById(any())).thenReturn(Optional.of(apiTestCaseJob));
        when(apiTestCaseJobRepository.save(any(ApiTestCaseJobEntity.class))).thenReturn(apiTestCaseJob);
        doNothing().when(apiTestCaseService).insertTestResult(anyString(), any());
        Boolean result = apiTestCaseJobService
            .insertJobReport(ApiTestCaseJobReport.builder().jobId(ObjectId.get().toString()).build());
        verify(apiTestCaseJobRepository, times(1)).save(any(ApiTestCaseJobEntity.class));
        assertThat(result).isTrue();
    }
}