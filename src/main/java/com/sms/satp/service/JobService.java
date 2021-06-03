package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.SceneCaseJobReport;

public interface JobService {

    void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport);

    void handleJobReport(SceneCaseJobReport sceneCaseJobReport);

    void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest);
}
