package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.BUILD_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_CASE_NOT_EXIST;
import static com.sms.courier.utils.Assert.notEmpty;

import com.sms.courier.common.annotation.JobServiceType;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.ApiTestCaseJobRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiTestCaseJobRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.DatabaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.webhook.WebhookEvent;
import com.sms.courier.webhook.enums.WebhookType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;

@Slf4j
@JobServiceType(type = JobType.CASE)
public class ApiTestCaseJobServiceImpl extends AbstractJobService<ApiTestCaseJobRepository> implements
    ApiTestCaseJobService {

    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository;
    private final ApiTestCaseService apiTestCaseService;
    private final JobMapper jobMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ApiTestCaseJobServiceImpl(ApiTestCaseJobRepository apiTestCaseJobRepository,
        CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        ProjectEnvironmentService projectEnvironmentService, ApiTestCaseService apiTestCaseService,
        CommonRepository commonRepository, JobMapper jobMapper,
        ApplicationEventPublisher applicationEventPublisher, EngineJobManagement engineJobManagement,
        DataBaseService dataBaseService) {
        super(apiTestCaseJobRepository, jobMapper, caseDispatcherService, projectEnvironmentService,
            engineJobManagement, commonRepository,dataBaseService);
        this.customizedApiTestCaseJobRepository = customizedApiTestCaseJobRepository;
        this.apiTestCaseService = apiTestCaseService;
        this.jobMapper = jobMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser) {
        try {
            handleJob(apiTestCaseJobRunRequest, currentUser, this::dispatcherJob);
        } catch (ApiTestPlatformException courierException) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", courierException.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
        }
    }


    @Override
    public Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest) {
        return customizedApiTestCaseJobRepository.page(pageRequest).map(jobMapper::toApiTestCaseJobPageResponse);
    }

    @Override
    public ApiTestCaseJobResponse get(String jobId) {
        return repository.findById(jobId).map(jobMapper::toApiTestCaseJobResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TEST_CASE_JOB_ERROR));
    }

    @Override
    public void apiTest(ApiTestRequest apiTestRequest, CustomUser currentUser) {
        try {
            ApiTestCaseJobEntity apiTestCaseJob = getApiTestCaseJobEntity(apiTestRequest, currentUser);
            repository.save(apiTestCaseJob);
            this.dispatcherJob(apiTestCaseJob);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
        }
    }


    @Override
    public ApiTestCaseJobResponse buildJob(ApiTestRequest request) {
        try {
            ApiTestCaseJobEntity apiTestCaseJobEntity = getApiTestCaseJobEntity(request, SecurityUtil.getCurrentUser());
            repository.save(apiTestCaseJobEntity);
            return jobMapper.toApiTestCaseJobResponse(apiTestCaseJobEntity);
        } catch (ApiTestPlatformException courierException) {
            log.error("Build the case job error. message:{}", courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Build the case job error.", e);
            throw new ApiTestPlatformException(BUILD_CASE_JOB_ERROR);
        }
    }

    @Override
    public List<ApiTestCaseJobResponse> buildJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        try {
            List<ApiTestCaseJobResponse> result = new ArrayList<>();
            handleJob(apiTestCaseJobRunRequest, SecurityUtil.getCurrentUser(),
                apiTestCaseJobEntity -> result.add(jobMapper.toApiTestCaseJobResponse(apiTestCaseJobEntity)));
            return result;
        } catch (ApiTestPlatformException courierException) {
            log.error("Build the case job error. message:{}", courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Build the case job error.", e);
            throw new ApiTestPlatformException(BUILD_CASE_JOB_ERROR);
        }
    }

    @Override
    public Boolean insertJobReport(ApiTestCaseJobReport jobReport) {
        repository.findById(jobReport.getJobId()).ifPresent(job -> {
            log.info("Insert job report. jobReport:{}", jobReport);
            setJobReport(jobReport, job);
            repository.save(job);
            saveTestResult(jobReport, job);
        });
        return true;
    }

    private ApiTestCaseJobEntity getApiTestCaseJobEntity(ApiTestRequest apiTestRequest, CustomUser currentUser) {
        JobEnvironment jobEnv = getJobEnv(apiTestRequest.getEnvId());
        JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestRequest);
        ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnv, jobApiTestCase, currentUser);
        apiTestCaseJob.setId(ObjectId.get().toString());
        apiTestCaseJob.setWorkspaceId(apiTestRequest.getWorkspaceId());
        apiTestCaseJob.setProjectId(apiTestRequest.getProjectId());
        return apiTestCaseJob;
    }

    private ApiTestCaseJobEntity createApiTestCaseJob(JobEnvironment jobEnvironment, JobApiTestCase jobApiTestCase,
        CustomUser currentUser) {
        return ApiTestCaseJobEntity.builder()
            .createDateTime(LocalDateTime.now())
            .modifyUserId(currentUser.getId())
            .createUserId(currentUser.getId())
            .jobStatus(JobStatus.PENDING)
            .createUserName(currentUser.getUsername())
            .environment(jobEnvironment)
            .apiTestCase(JobCaseApi.builder().jobApiTestCase(jobApiTestCase).build())
            .build();
    }

    private void handleJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser,
        Consumer<ApiTestCaseJobEntity> consumer) {
        DataCollectionRequest dataCollectionRequest = apiTestCaseJobRunRequest.getDataCollectionRequest();
        List<String> apiTestCaseIds = apiTestCaseJobRunRequest.getApiTestCaseIds();
        notEmpty(apiTestCaseIds, THE_CASE_NOT_EXIST);
        JobEnvironment jobEnv = getJobEnv(apiTestCaseJobRunRequest.getEnvId());
        for (String apiTestCaseId : apiTestCaseIds) {
            ApiTestCaseEntity apiTestCase = apiTestCaseService.findOne(apiTestCaseId);
            JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestCase);
            ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnv, jobApiTestCase, currentUser);
            apiTestCaseJob.setWorkspaceId(apiTestCaseJobRunRequest.getWorkspaceId());
            apiTestCaseJob.setProjectId(apiTestCase.getProjectId());
            apiTestCaseJob.setEmails(apiTestCaseJobRunRequest.getEmails());
            apiTestCaseJob.setNoticeType(apiTestCaseJobRunRequest.getNoticeType());
            // Multiple job are sent if a data collection exists.
            if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                .isNotEmpty(dataCollectionRequest.getDataList())) {
                JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                for (TestDataRequest dataList : dataCollectionRequest.getDataList()) {
                    apiTestCaseJob.setId(ObjectId.get().toString());
                    jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                    apiTestCaseJob.setDataCollection(jobDataCollection);
                    repository.save(apiTestCaseJob);
                    consumer.accept(apiTestCaseJob);
                }
            } else {
                apiTestCaseJob.setId(ObjectId.get().toString());
                repository.save(apiTestCaseJob);
                consumer.accept(apiTestCaseJob);
            }
        }
    }

    @Override
    @SuppressFBWarnings("BC_UNCONFIRMED_CAST")
    public void saveJobReport(JobReport jobReport, JobEntity job) {
        ApiTestCaseJobEntity apiTestCaseJob = (ApiTestCaseJobEntity) job;
        try {
            ApiTestCaseJobReport apiTestCaseJobReport = (ApiTestCaseJobReport) jobReport;
            setJobReport(apiTestCaseJobReport, apiTestCaseJob);
            repository.save(apiTestCaseJob);
            saveTestResult(apiTestCaseJobReport, apiTestCaseJob);
            caseDispatcherService
                .sendJobReport(apiTestCaseJob.getCreateUserId(),
                    jobMapper.toApiTestCaseJobReportResponse(apiTestCaseJobReport));
            applicationEventPublisher.publishEvent(WebhookEvent.create(WebhookType.CASE_REPORT,
                jobMapper.toWebhookCaseJobResponse(apiTestCaseJobReport)));
            applicationEventPublisher.publishEvent(jobMapper.toTestReportEvent(apiTestCaseJob));
        } catch (Exception e) {
            log.error("Save case job error!", e);
            caseDispatcherService
                .sendCaseErrorMessage(apiTestCaseJob.getCreateUserId(), "Save case report error!");
        }
    }

    private void saveTestResult(ApiTestCaseJobReport jobReport, ApiTestCaseJobEntity apiTestCaseJob) {
        String caseId = apiTestCaseJob.getApiTestCase().getJobApiTestCase().getId();
        if (StringUtils.isBlank(caseId)) {
            return;
        }
        TestResult testResult = new TestResult();
        testResult.setIsSuccess(JobStatus.SUCCESS == jobReport.getJobStatus() ? ResultType.SUCCESS : ResultType.FAIL);
        testResult.setJobId(apiTestCaseJob.getId());
        testResult.setTestUsername(apiTestCaseJob.getCreateUserName());
        testResult.setTestTime(apiTestCaseJob.getCreateDateTime());
        // Sava test result in api test case.
        apiTestCaseService.insertTestResult(caseId, testResult);
    }

    @Override
    public void onError(JobEntity jobEntity, boolean resend) {
        ApiTestCaseJobEntity job = (ApiTestCaseJobEntity) jobEntity;
        repository.deleteById(jobEntity.getId());
        if (resend) {
            log.error("Resend case job! {}", job);
            job.setId(ObjectId.get().toString());
            repository.save(job);
            engineJobManagement.dispatcherJob(job);
            return;
        }
        caseDispatcherService.sendCaseErrorMessage(job.getCreateUserId(), "No engine available!");
    }
}
