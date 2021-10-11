package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.THE_ENV_NOT_EXIST;
import static com.sms.courier.common.field.ScheduleField.LAST_TASK_COMPLETE_TIME;
import static com.sms.courier.common.field.ScheduleField.TASK_STATUS;
import static com.sms.courier.utils.Assert.notEmpty;
import static com.sms.courier.utils.Assert.notNull;

import com.sms.courier.common.enums.TaskStatus;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.AbstractCaseJobEntity;
import com.sms.courier.entity.job.common.AbstractSceneCaseJobEntity;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobEnvironment;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.schedule.JobRecord;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.JobService;
import com.sms.courier.service.ProjectEnvironmentService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

@Slf4j
public abstract class AbstractJobService<T extends MongoRepository<? extends JobEntity, String>> implements
    JobService {

    protected final T repository;
    protected final JobMapper jobMapper;
    protected final CaseDispatcherService caseDispatcherService;
    protected final ProjectEnvironmentService projectEnvironmentService;
    protected final CommonRepository commonRepository;

    public AbstractJobService(T repository, JobMapper jobMapper,
        CaseDispatcherService caseDispatcherService,
        ProjectEnvironmentService projectEnvironmentService,
        CommonRepository commonRepository) {
        this.repository = repository;
        this.jobMapper = jobMapper;
        this.caseDispatcherService = caseDispatcherService;
        this.projectEnvironmentService = projectEnvironmentService;
        this.commonRepository = commonRepository;
    }

    @Override
    public void handleJobReport(JobReport jobReport) {
        repository.findById(jobReport.getJobId()).ifPresent(job -> {
            log.info("Handle job report. jobReport:{}", jobReport);
            saveJobReport(jobReport, job);
        });
    }


    public abstract void saveJobReport(JobReport jobReport, JobEntity job);


    protected void setJobReport(ApiTestCaseJobReport jobReport, AbstractCaseJobEntity job) {
        JobApiTestCase jobApiTestCase = job.getApiTestCase().getJobApiTestCase();
        CaseReport caseReport = jobReport.getCaseReport();
        jobApiTestCase.setCaseReport(caseReport);
        populateJob(jobReport, job);
    }


    protected void setJobReport(SceneCaseJobReport jobReport, AbstractSceneCaseJobEntity job) {
        List<CaseReport> caseReportList = Objects
            .requireNonNullElse(jobReport.getCaseReportList(), Collections.emptyList());
        Map<String, CaseReport> caseReportMap =
            caseReportList.stream().distinct()
                .collect(Collectors.toMap(CaseReport::getCaseId, Function.identity()));
        if (MapUtils.isNotEmpty(caseReportMap)) {
            for (JobSceneCaseApi jobSceneCaseApi : job.getApiTestCase()) {
                JobApiTestCase jobApiTestCase = jobSceneCaseApi.getJobApiTestCase();
                jobApiTestCase.setCaseReport(caseReportMap.get(jobSceneCaseApi.getId()));
            }
        }
        populateJob(jobReport, job);
    }


    private void populateJob(JobReport jobReport, JobEntity job) {
        job.setJobStatus(jobReport.getJobStatus());
        job.setMessage(jobReport.getMessage());
        job.setTotalTimeCost(jobReport.getTotalTimeCost());
        job.setParamsTotalTimeCost(jobReport.getParamsTotalTimeCost());
        job.setInfoList(jobReport.getInfoList());
        job.setDelayTimeTotalTimeCost(jobReport.getDelayTimeTotalTimeCost());
    }

    protected JobEnvironment getJobEnv(String envId) {
        notEmpty(envId, THE_ENV_NOT_EXIST);
        ProjectEnvironmentEntity projectEnvironment = projectEnvironmentService.findOne(envId);
        notNull(projectEnvironment, THE_ENV_NOT_EXIST);
        return jobMapper.toJobEnvironment(projectEnvironment);
    }

    protected void updateScheduleTaskStatus(String scheduleId) {
        Query query = Query.query(Criteria.where(CommonField.ID.getName()).is(scheduleId));
        Update update = new Update();
        update.set(TASK_STATUS.getName(), TaskStatus.COMPLETE);
        update.set(LAST_TASK_COMPLETE_TIME.getName(), LocalDateTime.now());
        commonRepository.updateField(query, update, ScheduleEntity.class);
    }

    protected DataCollectionEntity getDataCollection(String dataCollId) {
        if (StringUtils.isNotBlank(dataCollId)) {
            return commonRepository.findById(dataCollId, DataCollectionEntity.class).orElse(null);
        }
        return null;
    }

    protected JobRecord createJobRecord(String caseId, String name) {
        return JobRecord.builder()
            .caseId(caseId)
            .caseName(name)
            .sceneCount(1).build();
    }

    protected ScheduleRecordEntity createScheduleRecord(ScheduleEntity scheduleEntity) {
        return ScheduleRecordEntity.builder()
            .id(ObjectId.get().toString())
            .scheduleId(scheduleEntity.getId())
            .scheduleName(scheduleEntity.getName())
            .createDateTime(LocalDateTime.now())
            .workspaceId(scheduleEntity.getWorkspaceId())
            .caseType(scheduleEntity.getCaseType())
            .jobRecords(new ArrayList<>())
            .execute(true)
            .projectId(scheduleEntity.getProjectId())
            .jobIds(new ArrayList<>())
            .version(1)
            .build();
    }

    protected Integer setSceneCaseApiData(SceneCaseApiEntity sceneCaseApi, List<JobSceneCaseApi> caseList,
        Integer index) {
        if (sceneCaseApi.getApiTestCase().isExecute()) {
            sceneCaseApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : sceneCaseApi.getOrder());
            caseList.add(jobMapper.toJobSceneCaseApi(sceneCaseApi));
            index = sceneCaseApi.getOrder();
        }
        return index;
    }

    protected Integer createIndex(SceneCaseApiEntity sceneCaseApi, List<JobSceneCaseApi> caseList, Integer index,
        List<CaseTemplateApiEntity> templateApiList) {
        Map<String, Boolean> isExecuteMap =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors
                    .toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isExecute));
        Map<String, Boolean> isLockMap =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isLock));
        for (CaseTemplateApiEntity caseTemplateApi : templateApiList) {
            caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
            caseTemplateApi.setCaseTemplateId(null);
            caseTemplateApi.getApiTestCase()
                .setExecute(isExecuteMap.getOrDefault(caseTemplateApi.getId(), Boolean.TRUE));
            caseTemplateApi.setLock(isLockMap.getOrDefault(caseTemplateApi.getId(), Boolean.FALSE));
            JobSceneCaseApi jobSceneCaseApi = jobMapper.toJobSceneCaseApiByTemplate(caseTemplateApi);
            jobSceneCaseApi.setSceneCaseId(sceneCaseApi.getSceneCaseId());
            caseList.add(jobSceneCaseApi);
            index = caseTemplateApi.getOrder();
        }
        return index;
    }
}
