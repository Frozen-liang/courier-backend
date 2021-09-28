package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.PENDING;
import static com.sms.courier.common.enums.JobStatus.RUNNING;
import static com.sms.courier.common.field.ApiTestCaseJobField.ENGINE_ID;
import static com.sms.courier.common.field.ApiTestCaseJobField.JOB_STATUS;

import com.google.common.collect.Lists;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.Field;
import com.sms.courier.common.listener.event.ScheduleJobRecordEvent;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.entity.schedule.CaseCondition;
import com.sms.courier.entity.schedule.JobRecord;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.repository.ScheduleSceneCaseJobRepository;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.ScheduleSceneCaseJobService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service(Constants.SCHEDULE_SCENE_CASE_SERVICE)
@Slf4j
public class ScheduleSceneCaseJobServiceImpl extends AbstractJobService<ScheduleSceneCaseJobRepository> implements
    ScheduleSceneCaseJobService {

    private final CommonRepository commonRepository;
    private final SceneCaseRepository sceneCaseRepository;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final ScheduleRecordRepository scheduleRecordRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ScheduleSceneCaseJobServiceImpl(ScheduleSceneCaseJobRepository repository, JobMapper jobMapper,
        CaseDispatcherService caseDispatcherService, ProjectEnvironmentService projectEnvironmentService,
        CommonRepository commonRepository,
        SceneCaseRepository sceneCaseRepository,
        SceneCaseApiRepository sceneCaseApiRepository,
        CaseTemplateApiRepository caseTemplateApiRepository,
        ScheduleRecordRepository scheduleRecordRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        super(repository, jobMapper, caseDispatcherService, projectEnvironmentService, commonRepository);
        this.commonRepository = commonRepository;
        this.sceneCaseRepository = sceneCaseRepository;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void saveJobReport(JobReport jobReport, JobEntity job) {
        try {
            ScheduleSceneCaseJobEntity scheduleCaseJob = (ScheduleSceneCaseJobEntity) job;
            SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
            setJobReport(sceneCaseJobReport, scheduleCaseJob);
            repository.save(scheduleCaseJob);
            applicationEventPublisher
                .publishEvent(ScheduleJobRecordEvent
                    .create(scheduleCaseJob.getScheduleRecordId(), job.getId(), scheduleCaseJob.getSceneCaseId(),
                        jobReport.getJobStatus()));
        } catch (Exception e) {
            log.error("Save schedule scene case job report error. jobId={}", jobReport.getJobId(), e);
        }
    }

    @Override
    public void reallocateJob(List<String> engineIds) {
        List<ScheduleSceneCaseJobEntity> scheduleCaseJobEntityList = repository
            .findByEngineIdInAndJobStatus(engineIds, JobStatus.RUNNING);
        try {
            if (CollectionUtils.isEmpty(scheduleCaseJobEntityList)) {
                return;
            }
            for (ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity : scheduleCaseJobEntityList) {
                this.dispatcherJob(scheduleSceneCaseJobEntity);
            }
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
        } catch (Exception e) {
            log.error("Reallocate schedule scene case job error!", e);
        }
    }

    @Override
    public void runningJobAck(RunningJobAck runningJobAck) {
        Map<Field, Object> updateField = Map.of(JOB_STATUS, RUNNING, ENGINE_ID, runningJobAck.getDestination());
        commonRepository
            .updateFieldById(runningJobAck.getJobId(), updateField, ScheduleSceneCaseJobEntity.class);
    }

    @Override
    public void dispatcherJob(JobEntity jobEntity) {
        try {
            ScheduleSceneCaseJobEntity scheduleCaseJobEntity = (ScheduleSceneCaseJobEntity) jobEntity;
            caseDispatcherService.dispatch(jobMapper.toScheduleSceneCaseJobResponse(scheduleCaseJobEntity));
        } catch (ApiTestPlatformException e) {
            log.error("Dispatcher schedule scene case job error.", e);
            SceneCaseJobReport caseJobReport = SceneCaseJobReport.builder().jobStatus(JobStatus.FAIL)
                .jobId(jobEntity.getId()).message(e.getMessage()).build();
            this.saveJobReport(caseJobReport, jobEntity);
        }
    }

    @Override
    public void schedule(ScheduleEntity scheduleEntity) {
        try {
            CaseFilter caseFilter = scheduleEntity.getCaseFilter();
            List<SceneCaseEntity> sceneCaseEntities = getSceneCaseEntity(caseFilter,
                scheduleEntity.getCaseCondition(), scheduleEntity.getCaseIds());
            JobEnvironment jobEnv = getJobEnv(scheduleEntity.getEnvId());
            ScheduleRecordEntity scheduleRecordEntity = createScheduleRecord(scheduleEntity);
            List<ScheduleSceneCaseJobEntity> scheduleSceneCaseJobEntities = new ArrayList<>();
            for (SceneCaseEntity sceneCaseEntity : sceneCaseEntities) {
                String sceneCaseId = sceneCaseEntity.getId();
                List<JobSceneCaseApi> apiCaseList = getApiCaseList(sceneCaseId);
                JobRecord jobRecord = createJobRecord(sceneCaseId, sceneCaseEntity.getName());
                DataCollectionEntity dataCollectionEntity = super.getDataCollection(sceneCaseEntity.getDataCollId());
                scheduleRecordEntity.getJobRecords().add(jobRecord);
                if (Objects.nonNull(dataCollectionEntity) && CollectionUtils
                    .isNotEmpty(dataCollectionEntity.getDataList())) {
                    List<TestData> dataList = dataCollectionEntity.getDataList();
                    jobRecord.setSceneCount(dataList.size());
                    for (TestData testData : dataList) {
                        JobDataCollection jobDataCollection = JobDataCollection.builder()
                            .collectionName(dataCollectionEntity.getCollectionName()).id(dataCollectionEntity.getId())
                            .testData(testData).projectId(dataCollectionEntity.getProjectId()).build();
                        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity =
                            getSceneCaseJobEntity(sceneCaseId, scheduleRecordEntity, jobEnv, apiCaseList);
                        scheduleSceneCaseJobEntity.setNext(sceneCaseEntity.isNext());
                        scheduleSceneCaseJobEntity.setDataCollection(jobDataCollection);
                        scheduleSceneCaseJobEntity.setName(testData.getDataName());
                        scheduleRecordEntity.getJobIds().add(scheduleSceneCaseJobEntity.getId());
                        scheduleSceneCaseJobEntities.add(scheduleSceneCaseJobEntity);
                    }

                } else {
                    ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity =
                        getSceneCaseJobEntity(sceneCaseId, scheduleRecordEntity, jobEnv, apiCaseList);
                    scheduleSceneCaseJobEntity.setNext(sceneCaseEntity.isNext());
                    scheduleSceneCaseJobEntity.setName(sceneCaseEntity.getName());
                    scheduleRecordEntity.getJobIds().add(scheduleSceneCaseJobEntity.getId());
                    scheduleSceneCaseJobEntities.add(scheduleSceneCaseJobEntity);
                }

            }
            scheduleRecordRepository.save(scheduleRecordEntity);
            if (CollectionUtils.isEmpty(sceneCaseEntities)) {
                super.updateScheduleTaskStatus(scheduleEntity.getId());
                return;
            }
            repository.saveAll(scheduleSceneCaseJobEntities);
            for (ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity : scheduleSceneCaseJobEntities) {
                this.dispatcherJob(scheduleSceneCaseJobEntity);
            }
        } catch (ApiTestPlatformException e) {
            log.error("Dispatcher schedule scene case job custom exception.", e);
        } catch (Exception e) {
            log.error("Dispatcher schedule scene case job system exception.", e);
        }

    }

    private List<SceneCaseEntity> getSceneCaseEntity(CaseFilter caseFilter, CaseCondition caseCondition,
        List<String> caseIds) {
        List<SceneCaseEntity> sceneCaseEntities;
        switch (caseFilter) {
            case ALL:
                sceneCaseEntities = sceneCaseRepository.findByRemovedIsFalse();
                break;
            case PRIORITY_AND_TAG:
                sceneCaseEntities = sceneCaseRepository.findByTagIdInAndPriorityIn(caseCondition.getTag(),
                    caseCondition.getPriority());
                break;
            case CUSTOM:
                sceneCaseEntities = sceneCaseRepository.findByIdIn(caseIds);
                break;
            default:
                sceneCaseEntities = Collections.emptyList();
        }
        return sceneCaseEntities;
    }

    private List<JobSceneCaseApi> getApiCaseList(String sceneCaseId) {
        List<JobSceneCaseApi> caseList = Lists.newArrayList();
        List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiRepository
            .findSceneCaseApiEntitiesBySceneCaseIdAndRemovedOrderByOrder(sceneCaseId, Boolean.FALSE);
        Integer index = 0;
        for (SceneCaseApiEntity sceneCaseApi : sceneCaseApiList) {
            if (Objects.isNull(sceneCaseApi.getCaseTemplateId())) {
                index = setSceneCaseApiData(sceneCaseApi, caseList, index);
            } else {
                index = setCaseTemplateApiData(sceneCaseApi, caseList, index);
            }

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


    private ScheduleSceneCaseJobEntity getSceneCaseJobEntity(String sceneCaseId, ScheduleRecordEntity scheduleRecord,
        JobEnvironment jobEnvironment, List<JobSceneCaseApi> caseList) {
        return ScheduleSceneCaseJobEntity.builder()
            .id(ObjectId.get().toString())
            .scheduleRecordId(scheduleRecord.getId())
            .projectId(scheduleRecord.getProjectId())
            .jobStatus(PENDING)
            .environment(jobEnvironment)
            .apiTestCase(caseList)
            .createDateTime(LocalDateTime.now())
            .modifyDateTime(LocalDateTime.now())
            .workspaceId(scheduleRecord.getWorkspaceId())
            .sceneCaseId(sceneCaseId)
            .build();
    }
}
