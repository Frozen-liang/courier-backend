package com.sms.courier.engine;


import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;

public interface EngineJobManagement {

    void dispatcherJob(SceneCaseJobEntity request);

    void dispatcherJob(ScheduleSceneCaseJobEntity request);

    void dispatcherJob(ApiTestCaseJobEntity request);

    void dispatcherJob(ScheduleCaseJobEntity request);

}
