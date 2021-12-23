package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CaseTemplateRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedCaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseJobRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.impl.SceneCaseJobServiceImpl;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DisplayName("Test cases for SceneCaseJobServiceTest")
class SceneCaseJobServiceTest {

    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final SceneCaseRepository sceneCaseRepository = mock(SceneCaseRepository.class);
    private final SceneCaseJobRepository sceneCaseJobRepository = mock(SceneCaseJobRepository.class);
    private final JobMapper jobMapper = mock(JobMapper.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        mock(CustomizedSceneCaseJobRepository.class);
    private final CaseDispatcherService caseDispatcherService = mock(CaseDispatcherService.class);
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository =
        mock(CustomizedCaseTemplateApiRepository.class);

    private final CaseTemplateRepository caseTemplateRepository = mock(CaseTemplateRepository.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final SceneCaseApiRepository sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
    private final DatabaseService dataBaseService = mock(DatabaseService.class);

    private final SceneCaseJobService sceneCaseJobService = new SceneCaseJobServiceImpl(
        projectEnvironmentService,
        sceneCaseRepository,
        sceneCaseJobRepository,
        jobMapper,
        customizedSceneCaseJobRepository,
        caseDispatcherService,
        customizedCaseTemplateApiRepository,
        caseTemplateRepository,
        caseTemplateApiRepository, sceneCaseApiRepository, commonRepository, applicationEventPublisher,
        dataBaseService);

    private final static String MOCK_ID = "1";
    private final static Integer MOCK_NUM = 1;
    private static final String ENGINE_ID = "/engine/13/invoke";
    private static final List<String> ENGINE_ID_LIST = Collections.singletonList(ENGINE_ID);
    private final SceneCaseJobEntity sceneCasejobEntity =
        SceneCaseJobEntity.builder().id(ObjectId.get().toString()).createUserId(ObjectId.get().toString()).build();
    private final CustomUser customUser = new CustomUser("username", "", Collections.emptyList(),
        ObjectId.get().toString(), "", "", TokenType.USER, LocalDate.now());

    private final static MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the runJob method in the SceneCaseJob service")
    void runJob_test() {
        ProjectEnvironmentEntity environment = ProjectEnvironmentEntity.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<CaseTemplateEntity> sceneCaseApi = Optional.ofNullable(CaseTemplateEntity.builder().build());
        when(caseTemplateRepository.findById(any())).thenReturn(sceneCaseApi);
        List<SceneCaseApiEntity> sceneCaseApiList1 = getSceneCaseApiList();
        when(sceneCaseApiRepository.findSceneCaseApiEntitiesBySceneCaseIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(sceneCaseApiList1);
        List<CaseTemplateApiEntity> templateApiList =
            Lists.newArrayList(
                CaseTemplateApiEntity.builder().id(MOCK_ID).apiTestCase(ApiTestCaseEntity.builder().build())
                    .order(MOCK_NUM).build());
        when(caseTemplateApiRepository.findAllByCaseTemplateIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(templateApiList);
        when(jobMapper.toJobSceneCaseApi(any()))
            .thenReturn(JobSceneCaseApi.builder().id(MOCK_ID).order(MOCK_NUM).build());
        JobSceneCaseApi jobSceneCaseApiList =
            JobSceneCaseApi.builder().id(MOCK_ID).order(MOCK_NUM).build();
        when(jobMapper.toJobSceneCaseApiByTemplate(any())).thenReturn(jobSceneCaseApiList);
        List<JobSceneCaseApi> caseApiList = Lists
            .newArrayList(JobSceneCaseApi.builder().id(MOCK_ID).order(MOCK_NUM).build());
        when(jobMapper.toJobSceneCaseApiListByTemplate(any())).thenReturn(caseApiList);

        JobDataCollection dataCollection1 = JobDataCollection.builder().build();
        when(jobMapper.toJobDataCollection(any())).thenReturn(dataCollection1);
        SceneCaseJobEntity sceneCaseJob = SceneCaseJobEntity.builder().id(MOCK_ID).build();
        when(sceneCaseJobRepository.save(any(SceneCaseJobEntity.class))).thenReturn(sceneCaseJob);
        AddSceneCaseJobRequest request = getAddRequest();
        sceneCaseJobService.runJob(request, customUser);
        verify(sceneCaseJobRepository, times(1)).save(any(SceneCaseJobEntity.class));
    }

    @Test
    @DisplayName("Test the runJob method in the SceneCaseJob service")
    void runJob_test_DataCollectionIsNull() {
        ProjectEnvironmentEntity environment = ProjectEnvironmentEntity.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        SceneCaseJobEntity sceneCaseJob = SceneCaseJobEntity.builder().id(MOCK_ID).build();
        when(sceneCaseJobRepository.save(any(SceneCaseJobEntity.class))).thenReturn(sceneCaseJob);
        AddSceneCaseJobRequest request = getAddRequest();
        request.setDataCollectionRequest(null);
        sceneCaseJobService.runJob(request, customUser);
        verify(sceneCaseJobRepository, times(1)).save(any(SceneCaseJobEntity.class));
    }

    @Test
    @DisplayName("Test the runJob method in the SceneCaseJob service thrown exception")
    void runJob_test_EnvironmentIsNull() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(any(), any());
        sceneCaseJobService.runJob(getAddRequest(), customUser);
        verify(projectEnvironmentService, times(1)).findOne(any());
    }

    @Test
    @DisplayName("Test the runJob method in the SceneCaseJob service thrown exception")
    void runJob_test_SceneCaseIsNull() {
        ProjectEnvironmentEntity environment = ProjectEnvironmentEntity.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        when(sceneCaseRepository.findById(any())).thenReturn(Optional.empty());
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(any(), any());
        sceneCaseJobService.runJob(getAddRequest(), customUser);
        verify(sceneCaseRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("An exception occurred while execute SceneCaseJob")
    public void environment_not_exist_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        sceneCaseJobService.runJob(getAddRequest(), customUser);
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendSceneCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("An exception occurred while execute SceneCaseJob")
    public void execute_exception_test() {
        when(projectEnvironmentService.findOne(any())).thenThrow(new RuntimeException());
        sceneCaseJobService.runJob(getAddRequest(), customUser);
        doNothing().when(caseDispatcherService).sendJobReport(anyString(), any(ApiTestCaseJobReportResponse.class));
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(anyString(), anyString());
        verify(caseDispatcherService, times(1)).sendSceneCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("Test the handleJobReport method in the SceneCaseJob service")
    void handleJobReport_test() {
        Optional<SceneCaseJobEntity> sceneCaseJob =
            Optional.ofNullable(
                SceneCaseJobEntity.builder()
                    .apiTestCase(Lists.newArrayList(JobSceneCaseApi.builder().id(MOCK_ID)
                        .jobApiTestCase(JobApiTestCase
                            .builder().id(MOCK_ID).build()).build()))
                    .id(MOCK_ID).build());
        when(sceneCaseJobRepository.findById(any())).thenReturn(sceneCaseJob);
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(any(), any());
        when(sceneCaseJobRepository.save(any())).thenReturn(SceneCaseJobEntity.builder().id(MOCK_ID).build());
        SceneCaseJobReport sceneCaseJobReport = getReport();
        sceneCaseJobService.handleJobReport(sceneCaseJobReport);
        verify(sceneCaseJobRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test the page method in the SceneCaseJob service")
    void page_test() {
        Page<SceneCaseJobEntity> sceneCaseJobPage = Page.empty(Pageable.unpaged());
        when(customizedSceneCaseJobRepository.page(any())).thenReturn(sceneCaseJobPage);
        SceneCaseJobRequest request =
            SceneCaseJobRequest.builder().sceneCaseId(MOCK_ID).userIds(Lists.newArrayList(MOCK_ID)).build();
        Page<SceneCaseJobResponse> responsePage = sceneCaseJobService.page(request);
        assertThat(responsePage).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the SceneCaseJob service thrown exception")
    void page_test_thrownException() {
        when(customizedSceneCaseJobRepository.page(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_JOB_PAGE_ERROR));
        SceneCaseJobRequest request =
            SceneCaseJobRequest.builder().sceneCaseId(MOCK_ID).userIds(Lists.newArrayList(MOCK_ID)).build();
        assertThatThrownBy(() -> sceneCaseJobService.page(request));
    }

    @Test
    @DisplayName("Test the get method in the SceneCaseJob service")
    void get_test() {
        Optional<SceneCaseJobEntity> sceneCaseJob = Optional
            .ofNullable(SceneCaseJobEntity.builder().id(MOCK_ID).build());
        when(sceneCaseJobRepository.findById(any())).thenReturn(sceneCaseJob);
        SceneCaseJobResponse response = SceneCaseJobResponse.builder().id(MOCK_ID).build();
        when(jobMapper.toSceneCaseJobResponse(any())).thenReturn(response);
        SceneCaseJobResponse dto = sceneCaseJobService.get(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the get method in the SceneCaseJob service thrown exception")
    void get_test_thrownException() {
        when(sceneCaseJobRepository.findOne(any())).thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_JOB_ERROR));
        assertThatThrownBy(() -> sceneCaseJobService.get(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the reallocateJob method in the SceneCaseJob service")
    public void reallocateJob_test() {
        when(sceneCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(sceneCasejobEntity));
        when(caseDispatcherService.dispatch(any(SceneCaseJobResponse.class))).thenReturn(ENGINE_ID);
        sceneCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(sceneCaseJobRepository, times(1)).save(any(SceneCaseJobEntity.class));
    }

    @Test
    @DisplayName("An apiTestPlatformException occurred while run reallocateJob in SceneCaseJob service")
    public void reallocateJob_ApiTestPlatformException_test() {
        when(sceneCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(sceneCasejobEntity));
        when(jobMapper.toSceneCaseJobResponse(sceneCasejobEntity)).thenReturn(SceneCaseJobResponse.builder().build());
        when(caseDispatcherService.dispatch(any(SceneCaseJobResponse.class)))
            .thenThrow(ExceptionUtils.mpe(""));
        sceneCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(caseDispatcherService, times(1)).sendSceneCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("An exception occurred while run reallocateJob in SceneCaseJob service")
    public void reallocateJob_Exception_test() {
        when(sceneCaseJobRepository.removeByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(List.of(sceneCasejobEntity));
        when(jobMapper.toSceneCaseJobResponse(sceneCasejobEntity)).thenReturn(SceneCaseJobResponse.builder().build());
        when(caseDispatcherService.dispatch(any(SceneCaseJobResponse.class)))
            .thenThrow(new RuntimeException());
        sceneCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(caseDispatcherService, times(1)).sendSceneCaseErrorMessage(anyString(), anyString());
    }

    @Test
    @DisplayName("An exception occurred while build job in SceneCaseJob service")
    public void buildJob_test() {
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        ProjectEnvironmentEntity environment = ProjectEnvironmentEntity.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        SceneCaseJobEntity sceneCaseJob = SceneCaseJobEntity.builder().id(MOCK_ID).build();
        when(sceneCaseJobRepository.save(any(SceneCaseJobEntity.class))).thenReturn(sceneCaseJob);
        AddSceneCaseJobRequest request = getAddRequest();
        request.setDataCollectionRequest(null);
        List<SceneCaseJobResponse> responses = sceneCaseJobService.buildJob(request);
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("Test the editReport method in the SceneCaseJob service")
    void editReport_test() {
        Optional<SceneCaseJobEntity> sceneCaseJob =
            Optional.ofNullable(
                SceneCaseJobEntity.builder()
                    .apiTestCase(Lists.newArrayList(JobSceneCaseApi.builder().id(MOCK_ID)
                        .jobApiTestCase(JobApiTestCase
                            .builder().id(MOCK_ID).build()).build()))
                    .id(MOCK_ID).build());
        when(sceneCaseJobRepository.findById(any())).thenReturn(sceneCaseJob);
        doNothing().when(caseDispatcherService).sendSceneCaseErrorMessage(any(), any());
        when(sceneCaseJobRepository.save(any())).thenReturn(SceneCaseJobEntity.builder().id(MOCK_ID).build());
        SceneCaseJobReport sceneCaseJobReport = getReport();
        Boolean isSuccess = sceneCaseJobService.editReport(sceneCaseJobReport);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the runningJobAck method in the SceneCaseJob service")
    public void runningJobAck_test() {
        when(commonRepository.updateFieldById(anyString(), any(), any())).thenReturn(true);
        RunningJobAck runningJobAck = new RunningJobAck();
        runningJobAck.setDestination(ENGINE_ID);
        runningJobAck.setJobId(ObjectId.get().toString());
        sceneCaseJobService.runningJobAck(runningJobAck);
        verify(commonRepository, times(1)).updateFieldById(anyString(), any(), any());
    }

    private AddSceneCaseJobRequest getAddRequest() {
        return AddSceneCaseJobRequest.builder()
            .sceneCaseId(MOCK_ID)
            .caseTemplateId(MOCK_ID)
            .envId(MOCK_ID)
            .dataCollectionRequest(
                DataCollectionRequest.builder().id(MOCK_ID)
                    .dataList(Lists.newArrayList(TestDataRequest.builder().build())).build())
            .build();
    }

    private List<SceneCaseApiEntity> getSceneCaseApiList() {
        return Lists.newArrayList(
            SceneCaseApiEntity.builder()
                .id(MOCK_ID)
                .order(MOCK_NUM)
                .apiTestCase(ApiTestCaseEntity.builder().id(MOCK_ID).execute(Boolean.TRUE).build())
                .build(),
            SceneCaseApiEntity.builder()
                .caseTemplateId(MOCK_ID)
                .order(MOCK_NUM)
                .apiTestCase(ApiTestCaseEntity.builder().id(MOCK_ID).execute(Boolean.TRUE).build())
                .caseTemplateApiConnList(
                    Lists.newArrayList(CaseTemplateApiConn.builder().caseTemplateApiId(MOCK_ID).execute(Boolean.TRUE)
                        .lock(Boolean.TRUE)
                        .build()))
                .build());
    }

    private SceneCaseJobReport getReport() {
        return SceneCaseJobReport.builder()
            .jobId(MOCK_ID)
            .jobStatus(JobStatus.SUCCESS)
            .caseReportList(Lists.newArrayList(CaseReport.builder().caseId(MOCK_ID).build()))
            .build();
    }
}
