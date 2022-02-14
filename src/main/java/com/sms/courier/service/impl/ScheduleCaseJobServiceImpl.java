package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.JobStatus.PENDING;

import com.sms.courier.common.annotation.JobServiceType;
import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.listener.event.ScheduleJobRecordEvent;
import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.schedule.CaseCondition;
import com.sms.courier.entity.schedule.JobRecord;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleCaseJobRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.service.DatabaseService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.ScheduleCaseJobService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

@JobServiceType(type = JobType.SCHEDULE_CATE)
@Slf4j
public class ScheduleCaseJobServiceImpl extends AbstractJobService<ScheduleCaseJobRepository> implements
    ScheduleCaseJobService {

    private final ApiTestCaseRepository apiTestCaseRepository;
    private final ScheduleRecordRepository scheduleRecordRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ScheduleCaseJobServiceImpl(ScheduleCaseJobRepository repository, JobMapper jobMapper,
        CaseDispatcherService caseDispatcherService, ProjectEnvironmentService projectEnvironmentService,
        CommonRepository commonRepository,
        ApiTestCaseRepository apiTestCaseRepository,
        ScheduleRecordRepository scheduleRecordRepository,
        ApplicationEventPublisher applicationEventPublisher, EngineJobManagement engineJobManagement,
        DatabaseService dataBaseService) {
        super(repository, jobMapper, caseDispatcherService, projectEnvironmentService, engineJobManagement,
            commonRepository, dataBaseService);
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @SuppressFBWarnings("BC_UNCONFIRMED_CAST")
    public void saveJobReport(JobReport jobReport, JobEntity job) {
        try {
            ScheduleCaseJobEntity scheduleCaseJob = (ScheduleCaseJobEntity) job;
            ApiTestCaseJobReport apiTestCaseJobReport = (ApiTestCaseJobReport) jobReport;
            setJobReport(apiTestCaseJobReport, scheduleCaseJob);
            repository.save(scheduleCaseJob);
            applicationEventPublisher
                .publishEvent(ScheduleJobRecordEvent.create(scheduleCaseJob.getScheduleRecordId(), job.getId(),
                    scheduleCaseJob.getApiTestCase().getJobApiTestCase().getId(), jobReport.getJobStatus()));
        } catch (Exception e) {
            log.error("Save schedule case job report error. jobId={}", jobReport.getJobId(), e);
        }
    }

    @Override
    public void onError(JobEntity jobEntity, boolean resend) {
        ScheduleCaseJobEntity job = (ScheduleCaseJobEntity) jobEntity;
        if (resend) {
            engineJobManagement.dispatcherJob(job);
            return;
        }
        ApiTestCaseJobReport caseJobReport = ApiTestCaseJobReport.builder().jobStatus(JobStatus.FAIL)
            .jobId(jobEntity.getId()).message("No engine available!").build();
        this.saveJobReport(caseJobReport, jobEntity);
    }

    @Override
    @Async("commonExecutor")
    public void schedule(ScheduleEntity scheduleEntity) {
        try {
            CaseFilter caseFilter = scheduleEntity.getCaseFilter();
            List<ApiTestCaseEntity> apiTestCaseEntities = getApiTestCaseEntity(scheduleEntity.getProjectId(),
                caseFilter, scheduleEntity.getCaseCondition(), scheduleEntity.getCaseIds());
            final JobEnvironment jobEnv = getJobEnv(scheduleEntity.getEnvIds().get(0));
            ScheduleRecordEntity scheduleRecordEntity = createScheduleRecord(scheduleEntity);
            List<ScheduleCaseJobEntity> scheduleCaseJobEntities = new ArrayList<>();
            for (ApiTestCaseEntity apiTestCaseEntity : apiTestCaseEntities) {
                final JobApiTestCase jobApiTestCase = jobMapper.toJobApiTestCase(apiTestCaseEntity);
                DataCollectionEntity dataCollectionEntity = getDataCollection(apiTestCaseEntity.getDataCollId());
                JobRecord jobRecord = createSceneCaseJobRecord(apiTestCaseEntity.getId(),
                    apiTestCaseEntity.getCaseName());
                scheduleRecordEntity.getJobRecords().add(jobRecord);
                if (CollectionUtils.isNotEmpty(dataCollectionEntity.getDataList())) {
                    List<TestData> dataList = dataCollectionEntity.getDataList();
                    jobRecord.setSceneCount(dataList.size());
                    for (TestData testData : dataList) {
                        ScheduleCaseJobEntity scheduleCaseJobEntity =
                            createScheduleCaseJob(scheduleRecordEntity, jobEnv, jobApiTestCase);
                        JobDataCollection jobDataCollection = JobDataCollection.builder()
                            .collectionName(dataCollectionEntity.getCollectionName()).id(dataCollectionEntity.getId())
                            .testData(testData).projectId(dataCollectionEntity.getProjectId()).build();
                        scheduleRecordEntity.getJobIds().add(scheduleCaseJobEntity.getId());
                        scheduleCaseJobEntity.setDataCollection(jobDataCollection);
                        scheduleCaseJobEntities.add(scheduleCaseJobEntity);
                    }
                } else {
                    ScheduleCaseJobEntity scheduleCaseJobEntity = createScheduleCaseJob(scheduleRecordEntity, jobEnv,
                        jobApiTestCase);
                    scheduleRecordEntity.getJobIds().add(scheduleCaseJobEntity.getId());
                    scheduleCaseJobEntities.add(scheduleCaseJobEntity);
                }
            }
            scheduleRecordRepository.save(scheduleRecordEntity);
            if (CollectionUtils.isEmpty(apiTestCaseEntities)) {
                super.updateScheduleTaskStatus(scheduleEntity.getId());
                return;
            }
            repository.saveAll(scheduleCaseJobEntities);
            for (ScheduleCaseJobEntity scheduleCaseJobEntity : scheduleCaseJobEntities) {
                dispatcherJob(scheduleCaseJobEntity);
            }
        } catch (ApiTestPlatformException e) {
            log.error("Dispatcher schedule job custom exception.", e);
        } catch (Exception e) {
            log.error("Dispatcher schedule job system exception.", e);
        }

    }

    private ScheduleCaseJobEntity createScheduleCaseJob(ScheduleRecordEntity scheduleRecord,
        JobEnvironment jobEnv,
        JobApiTestCase jobApiTestCase) {
        return ScheduleCaseJobEntity.builder()
            .id(ObjectId.get().toString())
            .name(jobApiTestCase.getCaseName())
            .scheduleRecordId(scheduleRecord.getId())
            .apiTestCase(JobCaseApi.builder().jobApiTestCase(jobApiTestCase).build())
            .createDateTime(LocalDateTime.now())
            .projectId(scheduleRecord.getProjectId())
            .workspaceId(scheduleRecord.getWorkspaceId())
            .environment(jobEnv)
            .jobStatus(PENDING)
            .build();
    }


    private List<ApiTestCaseEntity> getApiTestCaseEntity(String projectId, CaseFilter caseFilter,
        CaseCondition caseCondition,
        List<String> caseIds) {
        List<ApiTestCaseEntity> apiTestCaseEntities;
        switch (caseFilter) {
            case ALL:
                apiTestCaseEntities = apiTestCaseRepository.findByProjectIdIsAndRemovedIsFalse(projectId);
                break;
            case PRIORITY_AND_TAG:
                apiTestCaseEntities = apiTestCaseRepository
                    .findByTagIdInAndProjectId(caseCondition.getTag(), projectId);
                break;
            case CUSTOM:
                apiTestCaseEntities = apiTestCaseRepository.findByIdIn(caseIds);
                break;
            default:
                apiTestCaseEntities = Collections.emptyList();
        }
        return apiTestCaseEntities;
    }
}
