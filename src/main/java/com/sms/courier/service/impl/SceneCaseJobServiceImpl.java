package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.RUNNING;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_DATA_IS_NOT_BINDING_THE_ENV;
import static com.sms.courier.utils.Assert.isTrue;

import com.google.common.collect.Lists;
import com.sms.courier.common.annotation.JobServiceType;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.request.TestDataRequest;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.JobReport;
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
import com.sms.courier.service.DatabaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.SceneCaseJobService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.webhook.WebhookEvent;
import com.sms.courier.webhook.enums.WebhookType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;

@Slf4j
@JobServiceType(type = JobType.SCENE_CASE)
public class SceneCaseJobServiceImpl extends AbstractJobService<SceneCaseJobRepository> implements SceneCaseJobService {

    private final SceneCaseRepository sceneCaseRepository;
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;
    private final CaseTemplateRepository caseTemplateRepository;
    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CommonRepository commonRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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
        SceneCaseApiRepository sceneCaseApiRepository, CommonRepository commonRepository,
        ApplicationEventPublisher applicationEventPublisher, EngineJobManagement engineJobManagement,
        DatabaseService dataBaseService) {
        super(sceneCaseJobRepository, jobMapper, caseDispatcherService, projectEnvironmentService, engineJobManagement,
            commonRepository, dataBaseService);
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseJobRepository = customizedSceneCaseJobRepository;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
        this.caseTemplateRepository = caseTemplateRepository;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.commonRepository = commonRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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
        return repository.findById(jobId).map(jobMapper::toSceneCaseJobResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_JOB_ERROR));
    }

    @Override
    public void runJob(AddSceneCaseJobRequest request, CustomUser currentUser) {
        try {
            List<SceneCaseJobEntity> jobEntityList = getSceneCaseJobEntityList(request, currentUser);
            for (SceneCaseJobEntity sceneCaseJob : jobEntityList) {
                repository.save(sceneCaseJob);
                this.dispatcherJob(sceneCaseJob);
            }
        } catch (ApiTestPlatformException courierException) {
            log.error("Execute the SceneCaseJob error.", courierException);
            caseDispatcherService.sendSceneCaseErrorMessage(currentUser.getId(), courierException.getMessage());
        } catch (Exception e) {
            log.error("Execute the SceneCaseJob error.", e);
            caseDispatcherService.sendSceneCaseErrorMessage(currentUser.getId(), "Execute the SceneCaseJob error.");
        }
    }


    @Override
    public List<SceneCaseJobResponse> buildJob(AddSceneCaseJobRequest sceneCaseJobRequest) {
        List<SceneCaseJobEntity> jobEntityList = getSceneCaseJobEntityList(sceneCaseJobRequest,
            SecurityUtil.getCurrentUser());
        repository.saveAll(jobEntityList);
        return jobMapper.toSceneCaseJobResponseList(jobEntityList);
    }

    @Override
    public Boolean editReport(SceneCaseJobReport sceneCaseJobReport) {
        repository.findById(sceneCaseJobReport.getJobId()).ifPresent(job -> {
            setJobReport(sceneCaseJobReport, job);
            repository.save(job);
        });
        return Boolean.TRUE;
    }


    private List<SceneCaseJobEntity> getSceneCaseJobEntityList(AddSceneCaseJobRequest request, CustomUser currentUser) {
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(request.getEnvId());

        Assert.isTrue(Objects.nonNull(projectEnvironment), GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);

        JobEnvironment jobEnvironment = jobMapper.toJobEnvironment(projectEnvironment);
        SceneCaseEntity sceneCase = null;
        CaseTemplateEntity caseTemplate = null;
        if (Objects.nonNull(request.getSceneCaseId())) {
            sceneCase = sceneCaseRepository.findById(request.getSceneCaseId()).orElse(null);
        }
        if (Objects.nonNull(request.getCaseTemplateId())) {
            caseTemplate = caseTemplateRepository.findById(request.getCaseTemplateId()).orElse(null);
        }
        Assert.isTrue(Objects.nonNull(sceneCase) || Objects.nonNull(caseTemplate), GET_SCENE_CASE_BY_ID_ERROR);

        boolean next = Objects.isNull(sceneCase) ? caseTemplate.isNext() : sceneCase.isNext();
        List<JobSceneCaseApi> caseList = getApiCaseList(request);
        List<SceneCaseJobEntity> jobEntityList = Lists.newArrayList();
        if (Objects.isNull(request.getDataCollectionRequest())) {
            SceneCaseJobEntity sceneCaseJob = getSceneCaseJobEntity(request, currentUser, jobEnvironment,
                caseList);
            sceneCaseJob.setNext(next);
            sceneCaseJob.setEmails(request.getEmails());
            sceneCaseJob.setNoticeType(request.getNoticeType());
            sceneCaseJob.setName(Objects.nonNull(sceneCase) ? sceneCase.getName() : caseTemplate.getName());
            jobEntityList.add(sceneCaseJob);
        } else {
            DataCollectionEntity dataCollectionEntity =
                commonRepository.findById(request.getDataCollectionRequest().getId(),
                    DataCollectionEntity.class).orElse(null);
            if (Objects.nonNull(dataCollectionEntity) && Objects.nonNull(dataCollectionEntity.getEnvId())) {
                isTrue(Objects.equals(dataCollectionEntity.getEnvId(), request.getEnvId()),
                    THE_DATA_IS_NOT_BINDING_THE_ENV);
            }
            for (TestDataRequest testData : request.getDataCollectionRequest().getDataList()) {
                JobDataCollection jobDataCollection = jobMapper
                    .toJobDataCollection(request.getDataCollectionRequest());
                jobDataCollection.setTestData(jobMapper.toTestDataEntity(testData));
                SceneCaseJobEntity sceneCaseJob = getSceneCaseJobEntity(request, currentUser, jobEnvironment,
                    caseList);
                sceneCaseJob.setDataCollection(jobDataCollection);
                sceneCaseJob.setName(Objects.nonNull(sceneCase) ? sceneCase.getName() : caseTemplate.getName());
                sceneCaseJob.setNext(next);
                sceneCaseJob.setEmails(request.getEmails());
                sceneCaseJob.setNoticeType(request.getNoticeType());
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
                if (Objects.isNull(sceneCaseApi.getCaseTemplateId())) {
                    index = setSceneCaseApiData(sceneCaseApi, caseList, index);
                } else {
                    index = setCaseTemplateApiData(sceneCaseApi, caseList, index);
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

    private Integer setCaseTemplateApiData(SceneCaseApiEntity sceneCaseApi, List<JobSceneCaseApi> caseList,
        Integer index) {
        List<CaseTemplateApiEntity> templateApiList =
            caseTemplateApiRepository
                .findAllByCaseTemplateIdAndRemovedOrderByOrder(sceneCaseApi.getCaseTemplateId(),
                    Boolean.FALSE);
        return super.createIndex(sceneCaseApi, caseList, index, templateApiList);
    }

    @Override
    public void onError(JobEntity jobEntity, boolean resend) {
        SceneCaseJobEntity job = (SceneCaseJobEntity) jobEntity;
        repository.deleteById(jobEntity.getId());
        if (resend) {
            log.info("Resend scene case {}", jobEntity);
            job.setId(ObjectId.get().toString());
            repository.save(job);
            engineJobManagement.dispatcherJob(job);
            return;
        }
        caseDispatcherService.sendSceneCaseErrorMessage(job.getCreateUserId(), "No engine available!");
    }


    @Override
    @SuppressFBWarnings("BC_UNCONFIRMED_CAST")
    public void saveJobReport(JobReport jobReport, JobEntity job) {
        SceneCaseJobEntity sceneCaseJobEntity = (SceneCaseJobEntity) job;
        try {
            SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
            setJobReport(sceneCaseJobReport, sceneCaseJobEntity);
            repository.save(sceneCaseJobEntity);
            caseDispatcherService
                .sendJobReport(sceneCaseJobEntity.getCreateUserId(),
                    jobMapper.toSceneCaseJobReportResponse(sceneCaseJobReport));
            applicationEventPublisher.publishEvent(WebhookEvent.create(WebhookType.CASE_REPORT,
                jobMapper.toWebhookSceneCaseJobResponse(sceneCaseJobReport)));
            applicationEventPublisher
                .publishEvent(jobMapper.toTestReportEvent(sceneCaseJobEntity, sceneCaseJobReport.getCaseReportList()));
        } catch (Exception e) {
            log.error("Save scene case job error!", e);
            caseDispatcherService
                .sendSceneCaseErrorMessage(sceneCaseJobEntity.getCreateUserId(), "Save scene case report error!");
        }
    }
}
