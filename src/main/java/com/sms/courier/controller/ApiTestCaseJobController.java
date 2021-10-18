package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.API_TEST_CASE_JOB_PATH;

import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.response.ApiTestCaseJobPageResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.service.ApiTestCaseJobService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_TEST_CASE_JOB_PATH)
public class ApiTestCaseJobController {

    private final ApiTestCaseJobService apiTestCaseJobService;

    public ApiTestCaseJobController(ApiTestCaseJobService apiTestCaseJobService) {
        this.apiTestCaseJobService = apiTestCaseJobService;
    }

    @PostMapping("/page")
    public Page<ApiTestCaseJobPageResponse> page(@RequestBody ApiTestCaseJobPageRequest pageRequest) {
        return apiTestCaseJobService.page(pageRequest);
    }

    @PostMapping("/plug-in/build-job")
    public ApiTestCaseJobResponse buildJob(@RequestBody ApiTestRequest request) {
        return apiTestCaseJobService.buildJob(request);
    }

    @PostMapping("/plug-in/build-jobs")
    public List<ApiTestCaseJobResponse> buildJobs(@RequestBody ApiTestCaseJobRunRequest request) {
        return apiTestCaseJobService.buildJob(request);
    }

    @PostMapping("/plug-in/job-report")
    public Boolean insertJobReport(@RequestBody ApiTestCaseJobReport jobReport) {
        return apiTestCaseJobService.insertJobReport(jobReport);
    }

    @GetMapping("/{jobId}")
    public ApiTestCaseJobResponse get(@PathVariable String jobId) {
        return apiTestCaseJobService.get(jobId);
    }

}
