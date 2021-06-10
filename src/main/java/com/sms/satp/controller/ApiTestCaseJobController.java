package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.API_TEST_CASE_JOB_PATH;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.dto.response.ApiTestCaseJobPageResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.service.ApiTestCaseJobService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_TEST_CASE_JOB_PATH)
public class ApiTestCaseJobController {

    private final ApiTestCaseJobService apiTestCaseJobService;

    public ApiTestCaseJobController(ApiTestCaseJobService apiTestCaseJobService) {
        this.apiTestCaseJobService = apiTestCaseJobService;
    }

    @GetMapping("/page")
    public Page<ApiTestCaseJobPageResponse> page(ApiTestCaseJobPageRequest pageRequest) {
        return apiTestCaseJobService.page(pageRequest);
    }

    @GetMapping("/{jobId}")
    public ApiTestCaseJobResponse get(@PathVariable String jobId) {
        return apiTestCaseJobService.get(jobId);
    }

}
