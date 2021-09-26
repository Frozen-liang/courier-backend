package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.RUNNING;
import static com.sms.courier.common.exception.ErrorCode.BUILD_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_CASE_NOT_EXIST;
import static com.sms.courier.common.exception.ErrorCode.THE_ENV_NOT_EXIST;
import static com.sms.courier.common.field.ApiTestCaseJobField.ENGINE_ID;
import static com.sms.courier.common.field.ApiTestCaseJobField.JOB_STATUS;
import static com.sms.courier.utils.Assert.notEmpty;
import static com.sms.courier.utils.Assert.notNull;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.Field;
import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.ApiTestCaseJobRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiTestCaseJobRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiTestCaseJobServiceImpl implements ApiTestCaseJobService {

    private final ApiTestCaseJobRepository apiTestCaseJobRepository;
    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository;
    private final CaseDispatcherService caseDispatcherService;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final ApiTestCaseService apiTestCaseService;
    private final CommonRepository commonRepository;
    private final JobMapper jobMapper;

    public ApiTestCaseJobServiceImpl(ApiTestCaseJobRepository apiTestCaseJobRepository,
        CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        ProjectEnvironmentService projectEnvironmentService, ApiTestCaseService apiTestCaseService,
        CommonRepository commonRepository, JobMapper jobMapper) {
        this.apiTestCaseJobRepository = apiTestCaseJobRepository;
        this.customizedApiTestCaseJobRepository = customizedApiTestCaseJobRepository;
        this.caseDispatcherService = caseDispatcherService;
        this.projectEnvironmentService = projectEnvironmentService;
        this.apiTestCaseService = apiTestCaseService;
        this.commonRepository = commonRepository;
        this.jobMapper = jobMapper;
    }


    @Override
    public void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport) {
        log.info("Receive job report. jobReport:{}", apiTestCaseJobReport);
        apiTestCaseJobRepository.findById(apiTestCaseJobReport.getJobId()).ifPresent(job -> {
            log.info("Handle job report. jobReport:{}", apiTestCaseJobReport);
            updateJobReport(apiTestCaseJobReport, job);
            caseDispatcherService
                .sendJobReport(job.getCreateUserId(), jobMapper.toApiTestCaseJobReportResponse(apiTestCaseJobReport));
        });
    }

    @Override
    public void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser) {
        try {
            handleJob(apiTestCaseJobRunRequest, currentUser, this::dispatcherJob);
        } catch (ApiTestPlatformException courierException) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", courierException.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error.", e);
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
        }
    }


    @Override
    public Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest) {
        return customizedApiTestCaseJobRepository.page(pageRequest).map(jobMapper::toApiTestCaseJobPageResponse);
    }

    @Override
    public ApiTestCaseJobResponse get(String jobId) {
        return apiTestCaseJobRepository.findById(jobId).map(jobMapper::toApiTestCaseJobResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TEST_CASE_JOB_ERROR));
    }

    @Override
    public void apiTest(ApiTestRequest apiTestRequest, CustomUser currentUser) {
        try {
            ApiTestCaseJobEntity apiTestCaseJob = getApiTestCaseJobEntity(apiTestRequest, currentUser);
            apiTestCaseJobRepository.save(apiTestCaseJob);
            dispatcherJob(apiTestCaseJob);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error.", e);
            caseDispatcherService.sendCaseErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
        }
    }

    @Override
    public void reallocateJob(List<String> engineIds) {
        String userId = null;
        List<ApiTestCaseJobEntity> apiTestCaseJobList = apiTestCaseJobRepository
            .removeByEngineIdInAndJobStatus(engineIds, JobStatus.RUNNING);
        try {
            if (CollectionUtils.isEmpty(apiTestCaseJobList)) {
                return;
            }
            for (ApiTestCaseJobEntity apiTestCaseJobEntity : apiTestCaseJobList) {
                apiTestCaseJobEntity.setId(ObjectId.get().toString());
                apiTestCaseJobEntity.setCreateDateTime(LocalDateTime.now());
                userId = apiTestCaseJobEntity.getCreateUserId();
                apiTestCaseJobRepository.save(apiTestCaseJobEntity);
                dispatcherJob(apiTestCaseJobEntity);
            }
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            caseDispatcherService.sendCaseErrorMessage(userId, courierException.getMessage());
        } catch (Exception e) {
            log.error("Reallocate job  error.", e);
            caseDispatcherService.sendCaseErrorMessage(userId, "Execute the ApiTestCase error.");
        }
    }

    @Override
    public ApiTestCaseJobResponse buildJob(ApiTestRequest request) {
        try {
            ApiTestCaseJobEntity apiTestCaseJobEntity = getApiTestCaseJobEntity(request, SecurityUtil.getCurrentUser());
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
        apiTestCaseJobRepository.findById(jobReport.getJobId()).ifPresent(job -> {
            log.info("Insert job report. jobReport:{}", jobReport);
            updateJobReport(jobReport, job);
        });
        return true;
    }

    @Override
    public void runningJobAck(RunningJobAck runningJobAck) {
        Map<Field, Object> updateField = Map.of(JOB_STATUS, RUNNING, ENGINE_ID, runningJobAck.getDestination());
        commonRepository
            .updateFieldById(runningJobAck.getJobId(), updateField, ApiTestCaseJobEntity.class);
    }

    private void updateJobReport(ApiTestCaseJobReport jobReport, ApiTestCaseJobEntity job) {
        JobApiTestCase jobApiTestCase = job.getApiTestCase().getJobApiTestCase();
        CaseReport caseReport = jobReport.getCaseReport();
        jobApiTestCase.setCaseReport(caseReport);
        job.setJobStatus(jobReport.getJobStatus());
        job.setMessage(jobReport.getMessage());
        job.setTotalTimeCost(jobReport.getTotalTimeCost());
        job.setParamsTotalTimeCost(jobReport.getParamsTotalTimeCost());
        job.setInfoList(jobReport.getInfoList());
        job.setDelayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost());
        apiTestCaseJobRepository.save(job);
        caseReport = Objects
            .requireNonNullElse(caseReport, CaseReport.builder().errCode(jobReport.getErrCode())
                .failMessage(jobReport.getMessage()).isSuccess(
                    ResultType.FAIL).build());
        TestResult testResult = jobMapper.toTestResult(caseReport);
        testResult.setJobId(job.getId());
        testResult.setTestUsername(job.getCreateUserName());
        testResult.setTestTime(job.getCreateDateTime());
        // Sava test result in api test case.
        apiTestCaseService.insertTestResult(job.getApiTestCase().getJobApiTestCase().getId(), testResult);
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
            // Multiple job are sent if a data collection exists.
            if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                .isNotEmpty(dataCollectionRequest.getDataList())) {
                JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                for (TestDataRequest dataList : dataCollectionRequest.getDataList()) {
                    apiTestCaseJob.setId(ObjectId.get().toString());
                    jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                    apiTestCaseJob.setDataCollection(jobDataCollection);
                    apiTestCaseJobRepository.save(apiTestCaseJob);
                    consumer.accept(apiTestCaseJob);
                }
            } else {
                apiTestCaseJob.setId(ObjectId.get().toString());
                apiTestCaseJobRepository.save(apiTestCaseJob);
                consumer.accept(apiTestCaseJob);
            }
        }
    }

    private JobEnvironment getJobEnv(String envId) {
        notEmpty(envId, THE_ENV_NOT_EXIST);
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(envId);
        notNull(projectEnvironment, THE_ENV_NOT_EXIST);
        return jobMapper.toJobEnvironment(projectEnvironment);
    }

    private void dispatcherJob(ApiTestCaseJobEntity apiTestCaseJob) {
        try {
            caseDispatcherService.dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
        } catch (ApiTestPlatformException e) {
            apiTestCaseJobRepository.deleteById(apiTestCaseJob.getId());
            throw e;
        }
    }

}
