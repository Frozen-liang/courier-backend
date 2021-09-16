package com.sms.courier.service;

import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.security.pojo.CustomUser;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ApiTestCaseJobService {

    void handleJobReport(ApiTestCaseJobReport apiTestCaseJobReport);

    void runJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest, CustomUser currentUser);

    Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest);

    ApiTestCaseJobResponse get(String jobId);

    void apiTest(ApiTestRequest apiTestRequest, CustomUser currentUser);

    void reallocateJob(List<String> engineIds);

    ApiTestCaseJobResponse buildJob(ApiTestRequest request);

    List<ApiTestCaseJobResponse> buildJob(ApiTestCaseJobRunRequest apiTestCaseJobRunRequest);

    Boolean insertJobReport(ApiTestCaseJobReport jobReport);

    void runningJobAck(RunningJobAck runningJobAck);
}
