package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_CASE_NOT_EXIST;
import static com.sms.satp.common.exception.ErrorCode.THE_ENV_NOT_EXIST;
import static com.sms.satp.common.exception.ErrorCode.THE_REQUEST_ADDRESS_IS_ILLEGALITY;
import static com.sms.satp.utils.Assert.notEmpty;
import static com.sms.satp.utils.Assert.notNull;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.ApiTestRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.JobCaseApi;
import com.sms.satp.entity.job.common.CaseReport;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.repository.ApiTestCaseJobRepository;
import com.sms.satp.repository.CustomizedApiTestCaseJobRepository;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.ApiTestCaseJobService;
import com.sms.satp.service.ApiTestCaseService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.utils.ExceptionUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        });
    }

    @Override
    public void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser) {
        DataCollectionRequest dataCollectionRequest = apiTestCaseJobRunRequest.getDataCollectionRequest();
        AtomicReference<String> jobId = new AtomicReference<>();
        try {
            List<String> apiTestCaseIds = apiTestCaseJobRunRequest.getApiTestCaseIds();
            String envId = apiTestCaseJobRunRequest.getEnvId();
            notEmpty(apiTestCaseIds, THE_CASE_NOT_EXIST);
            notEmpty(envId, THE_ENV_NOT_EXIST);
            ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(envId);
            notNull(projectEnvironment, THE_ENV_NOT_EXIST);
            JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
            apiTestCaseIds.forEach((apiTestCaseId) -> {
                jobId.set(null);
                ApiTestCaseResponse apiTestCaseResponse = apiTestCaseService.findById(apiTestCaseId);
                ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnvironment, currentUser);
                apiTestCaseJob.setWorkspaceId(apiTestCaseJobRunRequest.getWorkspaceId());
                apiTestCaseJob.setApiTestCase(
                    JobCaseApi.builder().jobApiTestCase(jobMapper.toJobApiTestCase(apiTestCaseResponse)).build());
                // Multiple job are sent if a data collection exists.
                if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                    .isNotEmpty(dataCollectionRequest.getDataList())) {
                    JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                    dataCollectionRequest.getDataList().forEach(dataList -> {
                        apiTestCaseJob.setId(null);
                        jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                        apiTestCaseJob.setDataCollection(jobDataCollection);
                        apiTestCaseJobRepository.insert(apiTestCaseJob);
                        jobId.set(apiTestCaseJob.getId());
                        caseDispatcherService.dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
                    });
                } else {
                    apiTestCaseJobRepository.insert(apiTestCaseJob);
                    jobId.set(apiTestCaseJob.getId());
                    caseDispatcherService.dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
                }
            });
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", apiTestPlatEx.getMessage());
            errorHandler(currentUser.getId(), jobId, apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            errorHandler(currentUser.getId(), jobId, "Execute the ApiTestCase error");
        }
    }

    private ApiTestCaseJobEntity createApiTestCaseJob(JobEnvironment jobEnvironment,
        CustomUser currentUser) {
        return ApiTestCaseJobEntity.builder()
            .createDateTime(LocalDateTime.now())
            .modifyUserId(currentUser.getId())
            .createUserId(currentUser.getId())
            .createUserName(currentUser.getUsername())
            .modifyDateTime(LocalDateTime.now())
            .environment(jobEnvironment)
            .build();
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
            ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(apiTestRequest.getEnvId());
            String apiPath = apiTestRequest.getApiPath();

            if (Objects.isNull(projectEnvironment)) {
                checkApiPath(apiPath);
            }
            JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
            ApiTestCaseJobEntity apiTestCaseJob = createApiTestCaseJob(jobEnvironment, currentUser);
            apiTestCaseJob.setApiTestCase(
                JobCaseApi.builder().jobApiTestCase(jobMapper.toJobApiTestCase(apiTestRequest)).build());
            apiTestCaseJobRepository.insert(apiTestCaseJob);
            caseDispatcherService.dispatch(jobMapper.toApiTestCaseJobResponse(apiTestCaseJob));
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), "Execute the ApiTest error");
        }
    }

    private void checkApiPath(String apiPath) {
        if (requestProtocol.anyMatch(apiPath::startsWith)) {
            return;
        }
        throw ExceptionUtils.mpe(THE_REQUEST_ADDRESS_IS_ILLEGALITY);
    }

    private void errorHandler(String userId, AtomicReference<String> jobId, String message) {
        caseDispatcherService.sendErrorMessage(userId, message);
        if (StringUtils.isNotBlank(jobId.get())) {
            apiTestCaseJobRepository.deleteById(jobId.get());
        }
    }
}
