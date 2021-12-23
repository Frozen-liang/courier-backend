package com.sms.courier.common.enums;


import com.sms.courier.common.constant.Constants;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.JobEntity;

public enum JobType {
    CASE(Constants.CASE_SERVICE, "caseTask", ApiTestCaseJobEntity.class),
    SCHEDULE_CATE(Constants.SCHEDULE_CASE_SERVICE, "caseTask", ScheduleCaseJobEntity.class),
    SCENE_CASE(Constants.SCENE_CASE_SERVICE, "sceneCaseTask", SceneCaseJobEntity.class),
    SCHEDULER_SCENE_CASE(Constants.SCHEDULE_SCENE_CASE_SERVICE, "sceneCaseTask", ScheduleSceneCaseJobEntity.class);

    private final String serviceName;
    private final String taskType;
    private final Class<? extends JobEntity> entityClass;

    JobType(String serviceName, String taskType, Class<? extends JobEntity> entityClass) {
        this.serviceName = serviceName;
        this.taskType = taskType;
        this.entityClass = entityClass;
    }

    public Class<? extends JobEntity> getEntityClass() {
        return entityClass;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTaskType() {
        return taskType;
    }
}
