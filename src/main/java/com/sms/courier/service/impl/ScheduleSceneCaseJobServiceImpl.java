package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.PENDING;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.ExecuteType;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.common.listener.event.ScheduleJobRecordEvent;
import com.sms.courier.engine.EngineJobManagement;
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
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.entity.schedule.CaseCondition;
import com.sms.courier.entity.schedule.ExecuteRecord;
import com.sms.courier.entity.schedule.JobRecord;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedScheduleRecordRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.repository.ScheduleSceneCaseJobRepository;
import com.sms.courier.service.DatabaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.ScheduleSceneCaseJobService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.webhook.WebhookEvent;
import com.sms.courier.webhook.enums.WebhookType;
import com.sms.courier.webhook.response.WebhookScheduleResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleSceneCaseJobServiceImpl extends AbstractJobService implements
    ScheduleSceneCaseJobService {

    private final SceneCaseRepository sceneCaseRepository;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final ScheduleRecordRepository scheduleRecordRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CustomizedScheduleRecordRepository customizedScheduleRecordRepository;
    private final ScheduleSceneCaseJobRepository repository;

    public ScheduleSceneCaseJobServiceImpl(JobMapper jobMapper,
        CaseDispatcherService caseDispatcherService, ProjectEnvironmentService projectEnvironmentService,
        CommonRepository commonRepository,
        SceneCaseRepository sceneCaseRepository,
        SceneCaseApiRepository sceneCaseApiRepository,
        CaseTemplateApiRepository caseTemplateApiRepository,
        ScheduleRecordRepository scheduleRecordRepository,
        ApplicationEventPublisher applicationEventPublisher, EngineJobManagement engineJobManagement,
        DatabaseService dataBaseService,
        CustomizedScheduleRecordRepository customizedScheduleRecordRepository,
        ScheduleSceneCaseJobRepository scheduleSceneCaseJobRepository) {
        super(jobMapper, caseDispatcherService, projectEnvironmentService, engineJobManagement,
            commonRepository, dataBaseService);
        this.sceneCaseRepository = sceneCaseRepository;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.customizedScheduleRecordRepository = customizedScheduleRecordRepository;
        this.repository = scheduleSceneCaseJobRepository;
    }

    @Override
    @SuppressFBWarnings("BC_UNCONFIRMED_CAST")
    public void saveJobReport(JobReport jobReport, JobEntity job) {
        try {
            ScheduleSceneCaseJobEntity scheduleCaseJob = (ScheduleSceneCaseJobEntity) job;
            SceneCaseJobReport sceneCaseJobReport = (SceneCaseJobReport) jobReport;
            setJobReport(sceneCaseJobReport, scheduleCaseJob);
            repository.save(scheduleCaseJob);
            ScheduleRecordEntity recordEntity = scheduleRecordRepository.findById(scheduleCaseJob.getScheduleRecordId())
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_SCHEDULE_RECORD_BY_ID_ERROR));
            if (Objects.equals(ExecuteType.SERIAL, recordEntity.getExecuteType())) {
                ExecuteRecord executeRecord =
                    recordEntity.getExecuteRecord().stream().filter(record -> Objects.equals(scheduleCaseJob.getId(),
                        record.getJobId())).findFirst().orElse(null);
                ScheduleRecordEntity newRecordEntity = customizedScheduleRecordRepository
                    .findAndModifyExecuteRecord(recordEntity.getId(), scheduleCaseJob);
                executeSerialJob(executeRecord, newRecordEntity);
            }

            applicationEventPublisher
                .publishEvent(ScheduleJobRecordEvent
                    .create(scheduleCaseJob.getScheduleRecordId(), job.getId(), scheduleCaseJob.getSceneCaseId(),
                        jobReport.getJobStatus()));
        } catch (Exception e) {
            log.error("Save schedule scene case job report error. jobId={}", jobReport.getJobId(), e);
        }
    }

    @Override
    public void handleJobReport(JobReport jobReport) {
        repository.findById(jobReport.getJobId()).ifPresent(job -> {
            log.info("Handle job report. jobId:{} jobStatus:{}", jobReport.getJobId(), jobReport.getJobStatus());
            this.saveJobReport(jobReport, job);
        });
    }

    @Override
    public void onError(JobEntity jobEntity, boolean resend) {
        ScheduleSceneCaseJobEntity job = (ScheduleSceneCaseJobEntity) jobEntity;
        if (resend) {
            engineJobManagement.dispatcherJob(job);
            return;
        }
        SceneCaseJobReport caseJobReport = SceneCaseJobReport.builder().jobStatus(JobStatus.FAIL)
            .jobId(jobEntity.getId()).message("No engine available!").build();
        this.saveJobReport(caseJobReport, jobEntity);
    }

    @Override
    @Async("commonExecutor")
    public void schedule(ScheduleEntity scheduleEntity, String metadata) {
        try {
            CaseFilter caseFilter = scheduleEntity.getCaseFilter();
            List<SceneCaseEntity> sceneCaseEntities = getSceneCaseEntity(scheduleEntity.getProjectId(), caseFilter,
                scheduleEntity.getCaseCondition(), scheduleEntity.getCaseIds());
            List<JobEnvironment> jobEnvList = getJobEnvList(scheduleEntity.getEnvIds());
            ScheduleRecordEntity scheduleRecordEntity = createScheduleRecord(scheduleEntity, metadata);
            List<ScheduleSceneCaseJobEntity> scheduleSceneCaseJobEntities = new ArrayList<>();

            int order = 1;
            List<ExecuteRecord> executeRecordList = Lists.newArrayList();
            for (SceneCaseEntity sceneCaseEntity : sceneCaseEntities) {
                String sceneCaseId = sceneCaseEntity.getId();
                List<JobSceneCaseApi> apiCaseList = getApiCaseList(sceneCaseId);
                JobRecord jobRecord = createSceneCaseJobRecord(sceneCaseId, sceneCaseEntity.getName());
                jobRecord.setEnvCount(jobEnvList.size());
                scheduleRecordEntity.getJobRecords().add(jobRecord);
                for (JobEnvironment jobEnv : jobEnvList) {
                    DataCollectionEntity dataCollectionEntity = super
                        .getDataCollection(sceneCaseEntity, jobEnv.getId());
                    if (CollectionUtils.isNotEmpty(dataCollectionEntity.getDataList())) {
                        List<TestData> dataList = dataCollectionEntity.getDataList();
                        jobRecord.setSceneCount(dataList.size());
                        for (TestData testData : dataList) {
                            ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity =
                                getSceneCaseJobEntity(sceneCaseId, scheduleRecordEntity, jobEnv, apiCaseList);
                            scheduleSceneCaseJobEntity.setNext(sceneCaseEntity.isNext());
                            scheduleSceneCaseJobEntity
                                .setDataCollection(createJobDataCollection(dataCollectionEntity, testData));
                            scheduleSceneCaseJobEntity.setName(sceneCaseEntity.getName());
                            scheduleRecordEntity.getJobIds().add(scheduleSceneCaseJobEntity.getId());
                            if (Objects.equals(ExecuteType.SERIAL, scheduleEntity.getExecuteType())) {
                                executeRecordList.add(ExecuteRecord.builder().jobId(scheduleSceneCaseJobEntity.getId())
                                    .order(order)
                                    .build());
                            }
                            scheduleSceneCaseJobEntities.add(scheduleSceneCaseJobEntity);
                        }

                    } else {
                        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity =
                            getSceneCaseJobEntity(sceneCaseId, scheduleRecordEntity, jobEnv, apiCaseList);
                        scheduleSceneCaseJobEntity.setNext(sceneCaseEntity.isNext());
                        scheduleSceneCaseJobEntity.setName(sceneCaseEntity.getName());
                        scheduleRecordEntity.getJobIds().add(scheduleSceneCaseJobEntity.getId());
                        if (Objects.equals(ExecuteType.SERIAL, scheduleEntity.getExecuteType())) {
                            executeRecordList.add(ExecuteRecord.builder().jobId(scheduleSceneCaseJobEntity.getId())
                                .order(order)
                                .build());
                        }
                        scheduleSceneCaseJobEntities.add(scheduleSceneCaseJobEntity);
                    }
                }
                order++;
            }
            scheduleRecordEntity.setExecuteRecord(executeRecordList);

            scheduleRecordRepository.save(scheduleRecordEntity);
            if (CollectionUtils.isEmpty(sceneCaseEntities)) {
                super.updateScheduleTaskStatus(scheduleEntity.getId());
                return;
            }
            repository.saveAll(scheduleSceneCaseJobEntities);
            publishSchedulerStartEvent(scheduleEntity, scheduleRecordEntity.getMetadata());

            if (Objects.equals(ExecuteType.SERIAL, scheduleEntity.getExecuteType())) {
                List<String> jobIds = scheduleRecordEntity.getExecuteRecord().stream()
                    .filter(record -> Objects.equals(1, record.getOrder()))
                    .map(ExecuteRecord::getJobId).collect(Collectors.toList());
                scheduleSceneCaseJobEntities = scheduleSceneCaseJobEntities.stream()
                    .filter(job -> jobIds.contains(job.getId()))
                    .collect(Collectors.toList());
            }
            for (ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity : scheduleSceneCaseJobEntities) {
                this.dispatcherJob(scheduleSceneCaseJobEntity);
            }
        } catch (ApiTestPlatformException e) {
            log.error("Dispatcher schedule scene case job custom exception.", e);
        } catch (Exception e) {
            log.error("Dispatcher schedule scene case job system exception.", e);
        }

    }

    private void executeSerialJob(ExecuteRecord executeRecord, ScheduleRecordEntity newRecordEntity) {
        List<String> jobIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(newRecordEntity.getExecuteRecord())
            && newRecordEntity.getExecuteRecord().stream().noneMatch(record -> Objects.equals(executeRecord.getOrder(),
            record.getOrder()))) {
            jobIds = newRecordEntity.getExecuteRecord().stream()
                .filter(record -> Objects.equals(executeRecord.getOrder() + 1, record.getOrder()))
                .map(ExecuteRecord::getJobId).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(jobIds)) {
            Iterable<ScheduleSceneCaseJobEntity> jobEntities = repository.findAllById(jobIds);
            for (ScheduleSceneCaseJobEntity jobEntity : jobEntities) {
                this.dispatcherJob(jobEntity);
            }
        }
    }

    private void publishSchedulerStartEvent(ScheduleEntity scheduleEntity, Map<String, Object> metadata) {
        WebhookScheduleResponse webhookScheduleResponse = WebhookScheduleResponse.builder()
            .id(scheduleEntity.getId())
            .name(scheduleEntity.getName()).metadata(metadata).build();
        applicationEventPublisher
            .publishEvent(WebhookEvent.create(WebhookType.SCHEDULE_START, webhookScheduleResponse));
    }

    private JobDataCollection createJobDataCollection(DataCollectionEntity dataCollectionEntity, TestData testData) {
        return JobDataCollection.builder()
            .collectionName(dataCollectionEntity.getCollectionName())
            .id(dataCollectionEntity.getId())
            .testData(testData).projectId(dataCollectionEntity.getProjectId()).build();
    }

    private List<SceneCaseEntity> getSceneCaseEntity(String projectId, CaseFilter caseFilter,
        CaseCondition caseCondition, List<String> caseIds) {
        List<SceneCaseEntity> sceneCaseEntities;
        switch (caseFilter) {
            case ALL:
                sceneCaseEntities = sceneCaseRepository.findByProjectIdAndRemovedIsFalse(projectId);
                break;
            case PRIORITY_AND_TAG:
                sceneCaseEntities = findByTagIdInAndPriorityIn(projectId, caseCondition.getTag(),
                    caseCondition.getPriority());
                break;
            case CUSTOM:
                sceneCaseEntities = sceneCaseRepository.findByIdIn(caseIds);
                resetOrder(sceneCaseEntities, caseIds);
                break;
            default:
                sceneCaseEntities = Collections.emptyList();
        }
        return sceneCaseEntities;
    }

    private List<SceneCaseEntity> findByTagIdInAndPriorityIn(String projectId, List<String> tag,
        List<Integer> priority) {
        if (CollectionUtils.isNotEmpty(tag) && CollectionUtils.isNotEmpty(priority)) {
            return sceneCaseRepository.findByProjectIdAndTagIdInAndPriorityIn(projectId, tag, priority);
        }
        if (CollectionUtils.isNotEmpty(tag)) {
            return sceneCaseRepository.findByProjectIdAndTagIdIn(projectId, tag);
        }
        if (CollectionUtils.isNotEmpty(priority)) {
            return sceneCaseRepository.findByProjectIdAndPriorityIn(projectId, priority);
        }
        return Collections.emptyList();
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

        setJobDatabase(caseList);

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

    private void resetOrder(List<SceneCaseEntity> sceneCaseEntities, List<String> caseIds) {
        sceneCaseEntities.sort(((o1, o2) -> {
            int io1 = caseIds.indexOf(o1.getId());
            int io2 = caseIds.indexOf(o2.getId());

            if (io1 != -1) {
                io1 = sceneCaseEntities.size() - io1;
            }
            if (io2 != -1) {
                io2 = sceneCaseEntities.size() - io2;
            }
            return io2 - io1;
        }));
    }

}
