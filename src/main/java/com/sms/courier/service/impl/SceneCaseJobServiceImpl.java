package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.RUNNING;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;
import static com.sms.courier.common.field.SceneCaseJobField.ENGINE_ID;
import static com.sms.courier.common.field.SceneCaseJobField.JOB_STATUS;

import com.google.common.collect.Lists;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.Field;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.RunningJobAck;
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
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.SceneCaseJobService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseJobServiceImpl implements SceneCaseJobService {

    private final ProjectEnvironmentService projectEnvironmentService;
    private final SceneCaseRepository sceneCaseRepository;
    private final SceneCaseJobRepository sceneCaseJobRepository;
    private final JobMapper jobMapper;
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository;
    private final CaseDispatcherService caseDispatcherService;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;
    private final CaseTemplateRepository caseTemplateRepository;
    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CommonRepository commonRepository;

    public SceneCaseJobServiceImpl(
        ProjectEnvironmentService projectEnvironmentService,
        SceneCaseRepository sceneCaseRepository,
        SceneCaseJobRepository sceneCaseJobRepository,
        JobMapper jobMapper,
        CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository,
        CaseDispatcherService caseDispatcherService,
        CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository,
        CaseTemplateRepository caseTemplateRepository,
        CaseTemplateApiRepository caseTemplateApiRepository,
        SceneCaseApiRepository sceneCaseApiRepository, CommonRepository commonRepository) {
        this.projectEnvironmentService = projectEnvironmentService;
        this.sceneCaseRepository = sceneCaseRepository;
        this.sceneCaseJobRepository = sceneCaseJobRepository;
        this.jobMapper = jobMapper;
        this.customizedSceneCaseJobRepository = customizedSceneCaseJobRepository;
        this.caseDispatcherService = caseDispatcherService;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
        this.caseTemplateRepository = caseTemplateRepository;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.commonRepository = commonRepository;
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
            job.setParamsTotalTimeCost(jobReport.getParamsTotalTimeCost());
            job.setTotalTimeCost(jobReport.getTotalTimeCost());
            job.setInfoList(jobReport.getInfoList());
            job.setDelayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost());
            caseDispatcherService
                .sendJobReport(job.getCreateUserId(), jobMapper.toSceneCaseJobReportResponse(jobReport));
            sceneCaseJobRepository.save(job);
        });
    }

    @Override
    public void runJob(AddSceneCaseJobRequest request, CustomUser currentUser) {
        try {
            List<SceneCaseJobEntity> jobEntityList = getSceneCaseJobEntityList(request, currentUser);
            for (SceneCaseJobEntity sceneCaseJob : jobEntityList) {
                sceneCaseJobRepository.save(sceneCaseJob);
                dispatchJob(sceneCaseJob);
            }
        } catch (ApiTestPlatformException courierException) {
            log.error("Execute the SceneCaseJob error. errorMessage:{}", courierException.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the SceneCaseJob error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(currentUser.getId(), "Execute the SceneCaseJob error.");
        }
    }

    @Override
    public void reallocateJob(List<String> engineIds) {
        String userId = "";
        try {
            List<SceneCaseJobEntity> sceneCaseJobEntities = sceneCaseJobRepository
                .removeByEngineIdInAndJobStatus(engineIds, RUNNING);
            for (SceneCaseJobEntity sceneCaseJobEntity : sceneCaseJobEntities) {
                userId = sceneCaseJobEntity.getCreateUserId();
                sceneCaseJobEntity.setId(ObjectId.get().toString());
                sceneCaseJobEntity.setCreateDateTime(LocalDateTime.now());
                sceneCaseJobRepository.save(sceneCaseJobEntity);
                dispatchJob(sceneCaseJobEntity);
            }
        } catch (ApiTestPlatformException courierException) {
            log.error("Reallocate SceneCaseJob error. errorMessage:{}", courierException.getMessage());
            caseDispatcherService.sendErrorMessage(userId, courierException.getMessage());
        } catch (Exception e) {
            log.error("Reallocate SceneCaseJob error. errorMessage:{}", e.getMessage());
            caseDispatcherService.sendErrorMessage(userId, "Execute the SceneCaseJob error.");
        }
    }

    @Override
    public List<SceneCaseJobResponse> buildJob(AddSceneCaseJobRequest sceneCaseJobRequest) {
        List<SceneCaseJobEntity> jobEntityList = getSceneCaseJobEntityList(sceneCaseJobRequest,
            SecurityUtil.getCurrentUser());
        sceneCaseJobRepository.saveAll(jobEntityList);
        return jobMapper.toSceneCaseJobResponseList(jobEntityList);
    }

    @Override
    public Boolean editReport(SceneCaseJobReport sceneCaseJobReport) {
        handleJobReport(sceneCaseJobReport);
        return Boolean.TRUE;
    }

    @Override
    public void runningJobAck(RunningJobAck runningJobAck) {
        Map<Field, Object> updateField = Map.of(JOB_STATUS, RUNNING, ENGINE_ID, runningJobAck.getDestination());
        commonRepository
            .updateFieldById(runningJobAck.getJobId(), updateField, SceneCaseJobEntity.class);
    }

    private List<SceneCaseJobEntity> getSceneCaseJobEntityList(AddSceneCaseJobRequest request, CustomUser currentUser) {
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(request.getEnvId());
        if (Objects.isNull(projectEnvironment)) {
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
        JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
        SceneCaseEntity sceneCase = null;
        CaseTemplateEntity caseTemplate = null;
        if (Objects.nonNull(request.getSceneCaseId())) {
            sceneCase = sceneCaseRepository.findById(request.getSceneCaseId()).orElse(null);
        }
        if (Objects.nonNull(request.getCaseTemplateId())) {
            caseTemplate = caseTemplateRepository.findById(request.getCaseTemplateId()).orElse(null);
        }
        if (Objects.isNull(sceneCase) && Objects.isNull(caseTemplate)) {
            throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
        }
        boolean next = Objects.isNull(sceneCase) ? caseTemplate.isNext() : sceneCase.isNext();
        List<JobSceneCaseApi> caseList = getApiCaseList(request);
        List<SceneCaseJobEntity> jobEntityList = Lists.newArrayList();
        if (Objects.isNull(request.getDataCollectionRequest())) {
            SceneCaseJobEntity sceneCaseJob = getSceneCaseJobEntity(request, currentUser, jobEnvironment,
                caseList);
            sceneCaseJob.setNext(next);
            jobEntityList.add(sceneCaseJob);
        } else {
            for (TestDataRequest testData : request.getDataCollectionRequest().getDataList()) {
                JobDataCollection jobDataCollection = jobMapper
                    .toJobDataCollection(request.getDataCollectionRequest());
                jobDataCollection.setTestData(jobMapper.toTestDataEntity(testData));
                SceneCaseJobEntity sceneCaseJob = getSceneCaseJobEntity(request, currentUser, jobEnvironment,
                    caseList);
                sceneCaseJob.setDataCollection(jobDataCollection);
                sceneCaseJob.setNext(next);
                jobEntityList.add(sceneCaseJob);
            }
        }
        return jobEntityList;
    }

    private SceneCaseJobEntity getSceneCaseJobEntity(AddSceneCaseJobRequest request, CustomUser currentUser,
        JobEnvironment jobEnvironment, List<JobSceneCaseApi> caseList) {
        return SceneCaseJobEntity.builder()
            .id(ObjectId.get().toString())
            .projectId(request.getProjectId())
            .jobStatus(RUNNING)
            .environment(jobEnvironment)
            .apiTestCase(caseList)
            .createDateTime(LocalDateTime.now())
            .modifyDateTime(LocalDateTime.now())
            .workspaceId(request.getWorkspaceId())
            .createUserId(currentUser.getId())
            .modifyUserId(currentUser.getId())
            .createUserName(currentUser.getUsername())
            .sceneCaseId(request.getSceneCaseId())
            .caseTemplateId(request.getCaseTemplateId()).build();
    }

    private List<JobSceneCaseApi> getApiCaseList(AddSceneCaseJobRequest request) {
        List<JobSceneCaseApi> caseList = Lists.newArrayList();
        if (StringUtils.isNotBlank(request.getSceneCaseId())) {
            List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiRepository
                .findSceneCaseApiEntitiesBySceneCaseIdAndRemovedOrderByOrder(request.getSceneCaseId(), Boolean.FALSE);
            Integer index = 0;
            for (SceneCaseApiEntity sceneCaseApi : sceneCaseApiList) {
                if (Objects.isNull(sceneCaseApi.getCaseTemplateId())
                    && sceneCaseApi.getApiTestCase().isExecute()) {
                    sceneCaseApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : sceneCaseApi.getOrder());
                    caseList.add(jobMapper.toJobSceneCaseApi(sceneCaseApi));
                    index = sceneCaseApi.getOrder();

                } else {
                    List<CaseTemplateApiEntity> templateApiList =
                        caseTemplateApiRepository
                            .findAllByCaseTemplateIdAndRemovedOrderByOrder(sceneCaseApi.getCaseTemplateId(),
                                Boolean.FALSE);
                    for (CaseTemplateApiEntity caseTemplateApi : templateApiList) {
                        caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
                        caseTemplateApi.setCaseTemplateId(null);
                        JobSceneCaseApi jobSceneCaseApi = jobMapper.toJobSceneCaseApiByTemplate(caseTemplateApi);
                        jobSceneCaseApi.setSceneCaseId(sceneCaseApi.getSceneCaseId());
                        caseList.add(jobSceneCaseApi);
                        index = caseTemplateApi.getOrder();
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(request.getCaseTemplateId())) {
            List<CaseTemplateApiEntity> caseTemplateApiList = customizedCaseTemplateApiRepository
                .findByCaseTemplateIdAndIsExecuteAndIsRemove(request.getCaseTemplateId(), Boolean.TRUE, Boolean.FALSE);
            caseList.addAll(jobMapper.toJobSceneCaseApiListByTemplate(caseTemplateApiList));
        }

        if (CollectionUtils.isNotEmpty(caseList)) {
            caseList.sort(Comparator.comparingInt(JobSceneCaseApi::getOrder));
        }
        return caseList;
    }

    private void dispatchJob(SceneCaseJobEntity sceneCaseJob) {
        try {
            caseDispatcherService.dispatch(jobMapper.toSceneCaseJobResponse(sceneCaseJob));
        } catch (ApiTestPlatformException e) {
            sceneCaseJobRepository.deleteById(sceneCaseJob.getId());
            throw e;
        }
    }

}
