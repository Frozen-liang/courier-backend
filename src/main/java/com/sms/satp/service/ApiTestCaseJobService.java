package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.ApiTestRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.security.pojo.CustomUser;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ApiTestCaseJobService {

    void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport);

    void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser);

    Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest);

    ApiTestCaseJobResponse get(String jobId);

    void apiTest(ApiTestRequest apiTestRequest, CustomUser currentUser);

    void reallocateJob(List<String> engineIds);
}
