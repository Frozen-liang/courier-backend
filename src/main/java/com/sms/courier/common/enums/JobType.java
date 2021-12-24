package com.sms.courier.common.enums;

import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.JobEntity;

public enum JobType {
    CASE("caseTask", ApiTestCaseJobEntity.class),
    SCHEDULE_CATE("caseTask", ScheduleCaseJobEntity.class),
    SCENE_CASE("sceneCaseTask", SceneCaseJobEntity.class),
    SCHEDULER_SCENE_CASE("sceneCaseTask", ScheduleSceneCaseJobEntity.class);

    private final String taskType;
    private final Class<? extends JobEntity> entityClass;

    JobType(String taskType, Class<? extends JobEntity> entityClass) {
        this.taskType = taskType;
        this.entityClass = entityClass;
    }

    public Class<? extends JobEntity> getEntityClass() {
        return entityClass;
    }

    public String getTaskType() {
        return taskType;
    }
}
