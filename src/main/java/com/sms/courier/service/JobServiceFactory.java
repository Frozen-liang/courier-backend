package com.sms.courier.service;

import com.sms.courier.common.enums.JobType;

public interface JobServiceFactory {

    JobService getJobService(JobType jobType);

    ApiTestCaseJobService getApiTestCaseJobService();

    SceneCaseJobService getSceneCaseJobService();
}
