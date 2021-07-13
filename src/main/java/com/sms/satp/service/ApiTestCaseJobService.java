package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.ApiTestRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import org.springframework.data.domain.Page;

public interface ApiTestCaseJobService {

    void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport);

    void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, String currentUserId);

    Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest);

    ApiTestCaseJobResponse get(String jobId);

    void apiTest(ApiTestRequest apiTestRequest, String currentUserId);
}
