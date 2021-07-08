package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.JobSceneCaseApi;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.entity.job.common.CaseReport;
import com.sms.satp.entity.job.common.JobApiTestCase;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import com.sms.satp.repository.CustomizedSceneCaseJobRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseJobRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.service.SceneCaseJobService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseJobServiceImpl implements SceneCaseJobService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final SceneCaseRepository sceneCaseRepository;
    private final CaseTemplateConnRepository caseTemplateConnRepository;
    private final SceneCaseJobRepository sceneCaseJobRepository;
    private final JobMapper jobMapper;
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository;
    private final CaseDispatcherService caseDispatcherService;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;
    private final CaseTemplateApiService caseTemplateApiService;
    private final CaseTemplateRepository caseTemplateRepository;

    private static final String PREFIX = "/user/";

    public SceneCaseJobServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        ProjectEnvironmentService projectEnvironmentService,
        SceneCaseRepository sceneCaseRepository,
        CaseTemplateConnRepository caseTemplateConnRepository,
        SceneCaseJobRepository sceneCaseJobRepository,
        JobMapper jobMapper,
        CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository,
        CaseTemplateApiService caseTemplateApiService,
        CaseTemplateRepository caseTemplateRepository) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.projectEnvironmentService = projectEnvironmentService;
        this.sceneCaseRepository = sceneCaseRepository;
        this.caseTemplateConnRepository = caseTemplateConnRepository;
        this.sceneCaseJobRepository = sceneCaseJobRepository;
        this.jobMapper = jobMapper;
        this.customizedSceneCaseJobRepository = customizedSceneCaseJobRepository;
        this.caseDispatcherService = caseDispatcherService;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
        this.caseTemplateApiService = caseTemplateApiService;
        this.caseTemplateRepository = caseTemplateRepository;
    }

    @Override
    public Page<SceneCaseJobResponse> page(SceneCaseJobRequest sceneCaseJobRequest) {
        try {
            return customizedSceneCaseJobRepository.page(sceneCaseJobRequest).map(jobMapper::toSceneCaseJobResponse);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseJob page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_JOB_PAGE_ERROR);
        }
    }

    @Override
    public SceneCaseJobResponse get(String jobId) {
        return sceneCaseJobRepository.findById(jobId).map(jobMapper::toSceneCaseJobResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_JOB_ERROR));
    }

    @Override
    public void handleJobReport(SceneCaseJobReport jobReport) {
        Map<String, CaseReport> caseReportMap =
            jobReport.getCaseReportList().stream().distinct()
                .collect(Collectors.toMap(CaseReport::getCaseId, Function.identity()));
        sceneCaseJobRepository.findById(jobReport.getJobId()).ifPresent(job -> {
            for (JobSceneCaseApi jobSceneCaseApi : job.getApiTestCase()) {
                JobApiTestCase jobApiTestCase = jobSceneCaseApi.getJobApiTestCase();
                jobApiTestCase.setCaseReport(caseReportMap.get(jobSceneCaseApi.getId()));
            }
            job.setJobStatus(jobReport.getJobStatus());
            job.setMessage(jobReport.getMessage());
            caseDispatcherService.sendJobReport(PREFIX + job.getCreateUserId(), jobReport.getCaseReportList());
            sceneCaseJobRepository.save(job);
        });
    }

    @Override
    public void runJob(AddSceneCaseJobRequest request) {
        // getCurrentUserId
        int userId = 1;
        try {
            ProjectEnvironment projectEnvironment = projectEnvironmentService.findOne(request.getEnvId());
            if (Objects.isNull(projectEnvironment)) {
                throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);
            }
            JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
            Optional<SceneCase> sceneCase = Optional.empty();
            Optional<CaseTemplate> caseTemplate = Optional.empty();
            if (Objects.nonNull(request.getSceneCaseId())) {
                sceneCase = sceneCaseRepository.findById(request.getSceneCaseId());
            }
            if (Objects.nonNull(request.getCaseTemplateId())) {
                caseTemplate = caseTemplateRepository.findById(request.getCaseTemplateId());
            }
            if (sceneCase.isEmpty() && caseTemplate.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            List<JobSceneCaseApi> caseList = getApiCaseList(request);

            if (Objects.isNull(request.getDataCollectionRequest())) {
                SceneCaseJob sceneCaseJob = SceneCaseJob.builder().environment(jobEnvironment).apiTestCase(caseList)
                    .sceneCaseId(request.getSceneCaseId()).caseTemplateId(request.getCaseTemplateId()).build();
                sceneCaseJobRepository.insert(sceneCaseJob);
                caseDispatcherService.dispatch(sceneCaseJob);
            } else {
                for (TestDataRequest testData : request.getDataCollectionRequest().getDataList()) {
                    JobDataCollection jobDataCollection = jobMapper
                        .toJobDataCollection(request.getDataCollectionRequest());
                    jobDataCollection.setTestData(jobMapper.toTestDataEntity(testData));
                    SceneCaseJob sceneCaseJob = SceneCaseJob.builder()
                        .sceneCaseId(request.getSceneCaseId())
                        .caseTemplateId(request.getCaseTemplateId())
                        .environment(jobEnvironment)
                        .dataCollection(jobDataCollection)
                        .apiTestCase(caseList)
                        .build();
                    sceneCaseJobRepository.insert(sceneCaseJob);
                    caseDispatcherService.dispatch(sceneCaseJob);
                }
            }
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            caseDispatcherService.sendMessage(PREFIX + userId, apiTestPlatEx.getMessage());
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseJob!", e);
            e.printStackTrace();
            caseDispatcherService.sendMessage(PREFIX + userId, "Execute the SceneCaseJob error");
        }
    }

    private List<JobSceneCaseApi> getApiCaseList(AddSceneCaseJobRequest request) {
        List<JobSceneCaseApi> caseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(request.getSceneCaseApiIds())) {
            List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList();
            sceneCaseApiRepository.findAllById(request.getSceneCaseApiIds()).forEach(sceneCaseApiList::add);
            caseList.addAll(jobMapper.toJobSceneCaseApiList(sceneCaseApiList));
        }

        if (CollectionUtils.isNotEmpty(request.getCaseTemplateConnIds())) {
            List<CaseTemplateConn> caseTemplateConn = Lists.newArrayList();
            caseTemplateConnRepository.findAllById(request.getCaseTemplateConnIds()).forEach(caseTemplateConn::add);
            List<CaseTemplateApi> caseTemplateApiList = customizedCaseTemplateApiRepository.findByCaseTemplateIds(
                caseTemplateConn.stream().map(CaseTemplateConn::getCaseTemplateId).collect(Collectors.toList()));
            Map<String, Integer> apiConn = caseTemplateConn.stream()
                .map(CaseTemplateConn::getCaseTemplateApiConnList)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::getOrder));
            resetOrder(caseTemplateApiList, apiConn);
            caseList.addAll(jobMapper.toJobSceneCaseApiListByTemplate(caseTemplateApiList));
        }

        if (CollectionUtils.isNotEmpty(request.getCaseTemplateApiIds())) {
            List<CaseTemplateApi> caseTemplateApiList =
                caseTemplateApiService.listByCaseTemplateId(request.getCaseTemplateId());
            caseList.addAll(jobMapper.toJobSceneCaseApiListByTemplate(caseTemplateApiList));
        }

        if (CollectionUtils.isNotEmpty(caseList)) {
            caseList.sort(Comparator.comparingInt(JobSceneCaseApi::getOrder));
        }
        return caseList;
    }

    private void resetOrder(List<CaseTemplateApi> caseTemplateApiList,
        Map<String, Integer> apiConn) {
        for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
            caseTemplateApi.setOrder(apiConn.get(caseTemplateApi.getId()));
        }
    }

}
