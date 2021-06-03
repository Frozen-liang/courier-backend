package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.EXECUTE_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_ENVIRONMENT_NOT_EXITS_ERROR;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.entity.job.common.CaseReport;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.repository.ApiTestCaseJobRepository;
import com.sms.satp.service.ApiTestCaseService;
import com.sms.satp.service.JobService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    private final ApiTestCaseJobRepository apiTestCaseJobRepository;
    private final CaseDispatcherService caseDispatcherService;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final ApiTestCaseService apiTestCaseService;
    private final JobMapper jobMapper;
    private static final String PREFIX = "/user/";

    public JobServiceImpl(ApiTestCaseJobRepository apiTestCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        ProjectEnvironmentService projectEnvironmentService, ApiTestCaseService apiTestCaseService,
        JobMapper jobMapper) {
        this.apiTestCaseJobRepository = apiTestCaseJobRepository;
        this.caseDispatcherService = caseDispatcherService;
        this.projectEnvironmentService = projectEnvironmentService;
        this.apiTestCaseService = apiTestCaseService;
        this.jobMapper = jobMapper;
    }


    @Override
    public void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport) {
        apiTestCaseJobRepository.findById(apiTestCaseJobReport.getJobId()).ifPresent(job -> {
            log.info("Handle job report. jobReport:{}", apiTestCaseJobReport.toString());
            JobApiTestCase apiTestCase = job.getApiTestCase();
            CaseReport caseReport = apiTestCaseJobReport.getCaseReport();
            apiTestCase.setRuntime(caseReport.getRuntime());
            apiTestCase.setResponse(caseReport.getResponse());
            apiTestCase.setResult(caseReport.getResult());
            apiTestCase.setFailMessage(caseReport.getFailMessage());
            apiTestCase.setParamData(caseReport.getParamData());
            job.setJobStatus(apiTestCaseJobReport.getJobStatus());
            job.setMessage(apiTestCaseJobReport.getMessage());
            caseDispatcherService.sendJobReport(PREFIX + job.getCreateUserId(), job);
            apiTestCaseJobRepository.save(job);
        });
    }

    @Override
    public void handleJobReport(SceneCaseJobReport sceneCaseJobReport) {

    }

    @Override
    public void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        DataCollectionRequest dataCollectionRequest = apiTestCaseJobRunRequest.getDataCollectionRequest();
        try {
            ApiTestCaseResponse apiTestCaseResponse = apiTestCaseService
                .findById(apiTestCaseJobRunRequest.getApiTestCaseId());
            ProjectEnvironment projectEnvironment = projectEnvironmentService
                .findOne(apiTestCaseJobRunRequest.getEnvId());
            if (Objects.isNull(projectEnvironment)) {
                throw ExceptionUtils.mpe(THE_ENVIRONMENT_NOT_EXITS_ERROR);
            }
            ApiTestCaseJob apiTestCaseJob = ApiTestCaseJob.builder()
                .apiTestCase(jobMapper.toJobApiTestCase(apiTestCaseResponse))
                .environment(jobMapper.toJobEnvironment(projectEnvironment))
                .build();
            if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                .isNotEmpty(dataCollectionRequest.getDataList())) {
                JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                dataCollectionRequest.getDataList().forEach(dataList -> {
                    jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                    apiTestCaseJob.setDataCollection(jobDataCollection);
                    apiTestCaseJob.setId(null);
                    apiTestCaseJobRepository.insert(apiTestCaseJob);
                    caseDispatcherService.dispatch(apiTestCaseJob);
                });
                return;
            }
            apiTestCaseJobRepository.insert(apiTestCaseJob);
            caseDispatcherService.dispatch(apiTestCaseJob);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error");
            throw ExceptionUtils.mpe(EXECUTE_API_TEST_CASE_ERROR);
        }
    }
}
