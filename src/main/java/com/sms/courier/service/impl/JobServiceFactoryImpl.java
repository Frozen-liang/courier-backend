package com.sms.courier.service.impl;

import com.sms.courier.common.enums.JobType;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.JobService;
import com.sms.courier.service.JobServiceFactory;
import com.sms.courier.service.SceneCaseJobService;
import com.sms.courier.service.ScheduleCaseJobService;
import com.sms.courier.service.ScheduleSceneCaseJobService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class JobServiceFactoryImpl implements JobServiceFactory {

    private final ApiTestCaseJobService apiTestCaseJobService;
    private final SceneCaseJobService sceneCaseJobService;
    private final ScheduleCaseJobService scheduleCaseJobService;
    private final ScheduleSceneCaseJobService scheduleSceneCaseJobService;
    private final Map<JobType, JobService> jobServiceMap = new HashMap<>();

    public JobServiceFactoryImpl(ApiTestCaseJobService apiTestCaseJobService,
        SceneCaseJobService sceneCaseJobService, ScheduleCaseJobService scheduleCaseJobService,
        ScheduleSceneCaseJobService scheduleSceneCaseJobService) {
        this.apiTestCaseJobService = apiTestCaseJobService;
        this.sceneCaseJobService = sceneCaseJobService;
        this.scheduleCaseJobService = scheduleCaseJobService;
        this.scheduleSceneCaseJobService = scheduleSceneCaseJobService;
    }

    @PostConstruct
    public void setJobServiceMap() {
        jobServiceMap.put(JobType.CASE, apiTestCaseJobService);
        jobServiceMap.put(JobType.SCENE_CASE, sceneCaseJobService);
        jobServiceMap.put(JobType.SCHEDULE_CATE, scheduleCaseJobService);
        jobServiceMap.put(JobType.SCHEDULER_SCENE_CASE, scheduleSceneCaseJobService);
    }

    @Override
    public JobService getJobService(JobType jobType) {
        if (!jobServiceMap.containsKey(jobType)) {
            throw new IllegalArgumentException(String.format("The job type [%s] is not supported.", jobType));
        }

        return jobServiceMap.get(jobType);
    }

    @Override
    public ApiTestCaseJobService getApiTestCaseJobService() {
        return (ApiTestCaseJobService) jobServiceMap.get(JobType.CASE);

    }

    @Override
    public SceneCaseJobService getSceneCaseJobService() {
        return (SceneCaseJobService) jobServiceMap.get(JobType.SCENE_CASE);
    }
}
