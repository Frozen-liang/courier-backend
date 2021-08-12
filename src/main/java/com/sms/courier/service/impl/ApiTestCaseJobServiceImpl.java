package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_CASE_NOT_EXIST;
import static com.sms.courier.common.exception.ErrorCode.THE_ENV_NOT_EXIST;
import static com.sms.courier.common.exception.ErrorCode.THE_REQUEST_ADDRESS_IS_ILLEGALITY;
import static com.sms.courier.utils.Assert.notEmpty;
import static com.sms.courier.utils.Assert.notNull;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.ResultType;
import com.sms.courier.common.exception.ApiTestPlatformException;
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
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.ApiTestCaseJobRepository;
import com.sms.courier.repository.CustomizedApiTestCaseJobRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
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
    private final JobMapper jobMapper;
    private final Stream<String> requestProtocol = Stream.of("Http", "Https", "ws", "wss");

    public ApiTestCaseJobServiceImpl(ApiTestCaseJobRepository apiTestCaseJobRepository,
        CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        ProjectEnvironmentService projectEnvironmentService, ApiTestCaseService apiTestCaseService,
        JobMapper jobMapper) {
        this.apiTestCaseJobRepository = apiTestCaseJobRepository;
        this.customizedApiTestCaseJobRepository = customizedApiTestCaseJobRepository;
        this.caseDispatcherService = caseDispatcherService;
        this.projectEnvironmentService = projectEnvironmentService;
        this.apiTestCaseService = apiTestCaseService;
        this.jobMapper = jobMapper;
    }


    @Override
    public void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport) {
        apiTestCaseJobRepository.findById(apiTestCaseJobReport.getJobId()).ifPresent(job -> {
            log.info("Handle job report. jobReport:{}", apiTestCaseJobReport);
            JobApiTestCase jobApiTestCase = job.getApiTestCase().getJobApiTestCase();
            CaseReport caseReport = apiTestCaseJobReport.getCaseReport();
            jobApiTestCase.setCaseReport(caseReport);
            job.setJobStatus(apiTestCaseJobReport.getJobStatus());
            job.setMessage(apiTestCaseJobReport.getMessage());
            job.setTotalTimeCost(apiTestCaseJobReport.getTotalTimeCost());
            caseDispatcherService
                .sendJobReport(job.getCreateUserId(), jobMapper.toApiTestCaseJobReportResponse(apiTestCaseJobReport));
            apiTestCaseJobRepository.save(job);
            caseReport = Objects
                .requireNonNullElse(caseReport, CaseReport.builder().errCode(apiTestCaseJobReport.getErrCode())
                    .failMessage(apiTestCaseJobReport.getMessage()).isSuccess(
                        ResultType.FAIL).build());
            TestResult testResult = jobMapper.toTestResult(caseReport);
            testResult.setJobId(job.getId());
            testResult.setTestUsername(job.getCreateUserName());
            testResult.setTestTime(job.getCreateDateTime());
            // Sava test result in api test case.
            apiTestCaseService.insertTestResult(job.getApiTestCase().getJobApiTestCase().getId(), testResult);
        });
    }

    @Override
    public void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser) {
        DataCollectionRequest dataCollectionRequest = apiTestCaseJobRunRequest.getDataCollectionRequest();
        try {
            List<String> apiTestCaseIds = apiTestCaseJobRunRequest.getApiTestCaseIds();
            String envId = apiTestCaseJobRunRequest.getEnvId();
            notEmpty(apiTestCaseIds, THE_CASE_NOT_EXIST);
            notEmpty(envId, THE_ENV_NOT_EXIST);
            ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(envId);
            notNull(projectEnvironment, THE_ENV_NOT_EXIST);
            JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
            for (String apiTestCaseId : apiTestCaseIds) {
                ApiTestCaseEntity apiTestCase = apiTestCaseService.findOne(apiTestCaseId);
                ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnvironment, currentUser);
                apiTestCaseJob.setWorkspaceId(apiTestCaseJobRunRequest.getWorkspaceId());
                apiTestCaseJob.setProjectId(apiTestCase.getProjectId());
                apiTestCaseJob.setApiTestCase(
                    JobCaseApi.builder().jobApiTestCase(jobMapper.toJobApiTestCase(apiTestCase)).build());
                // Multiple job are sent if a data collection exists.
                if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                    .isNotEmpty(dataCollectionRequest.getDataList())) {
                    JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                    for (TestDataRequest dataList : dataCollectionRequest.getDataList()) {
                        String id = ObjectId.get().toString();
                        apiTestCaseJob.setId(id);
                        jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                        apiTestCaseJob.setDataCollection(jobDataCollection);
                        String engineId = caseDispatcherService
                            .dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
                        apiTestCaseJob.setEngineId(engineId);
                        apiTestCaseJobRepository.save(apiTestCaseJob);
                    }
                } else {
                    apiTestCaseJob.setId(ObjectId.get().toString());
                    String engineId = caseDispatcherService
                        .dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
                    apiTestCaseJob.setEngineId(engineId);
                    apiTestCaseJobRepository.save(apiTestCaseJob);
                }
            }
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", apiTestPlatEx.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
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
            String engineId = caseDispatcherService.dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
            apiTestCaseJob.setEngineId(engineId);
            apiTestCaseJobRepository.save(apiTestCaseJob);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), "Execute the ApiTestCase error.");
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
                String engineId = caseDispatcherService
                    .dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJobEntity));
                apiTestCaseJobEntity.setEngineId(engineId);
                apiTestCaseJobRepository.save(apiTestCaseJobEntity);
            }
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            caseDispatcherService.sendErrorMessage(userId, apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Reallocate job  errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(userId, "Execute the ApiTestCase error.");
        }
    }

    @Override
    public ApiTestCaseJobResponse buildJob(ApiTestRequest request) {
        ApiTestCaseJobEntity apiTestCaseJobEntity = getApiTestCaseJobEntity(request, SecurityUtil.getCurrentUser());
        return jobMapper.toApiTestCaseJobResponse(apiTestCaseJobEntity);
    }

    private ApiTestCaseJobEntity getApiTestCaseJobEntity(ApiTestRequest apiTestRequest, CustomUser currentUser) {
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(apiTestRequest.getEnvId());
        String apiPath = apiTestRequest.getApiPath();
        if (Objects.isNull(projectEnvironment)) {
            checkApiPath(apiPath);
        }
        JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
        ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnvironment, currentUser);
        apiTestCaseJob.setId(ObjectId.get().toString());
        apiTestCaseJob.setWorkspaceId(apiTestRequest.getWorkspaceId());
        apiTestCaseJob.setProjectId(apiTestRequest.getProjectId());
        apiTestCaseJob.setApiTestCase(
            JobCaseApi.builder().jobApiTestCase(jobMapper.toJobApiTestCase(apiTestRequest)).build());
        return apiTestCaseJob;
    }

    private ApiTestCaseJobEntity createApiTestCaseJob(JobEnvironment jobEnvironment,
        CustomUser currentUser) {
        return ApiTestCaseJobEntity.builder()
            .createDateTime(LocalDateTime.now())
            .modifyUserId(currentUser.getId())
            .createUserId(currentUser.getId())
            .jobStatus(JobStatus.RUNNING)
            .createUserName(currentUser.getUsername())
            .environment(jobEnvironment)
            .build();
    }

    private void checkApiPath(String apiPath) {
        if (requestProtocol.anyMatch(apiPath::startsWith)) {
            return;
        }
        throw ExceptionUtils.mpe(THE_REQUEST_ADDRESS_IS_ILLEGALITY);
    }
}
