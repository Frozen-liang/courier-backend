package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChannelController {

    private final JobService jobService;
    private final EngineMemberManagement engineMemberManagement;

    public ChannelController(JobService jobService, EngineMemberManagement engineMemberManagement) {
        this.jobService = jobService;
        this.engineMemberManagement = engineMemberManagement;
    }

    @MessageMapping(Constants.SDK_VERSION + "/job-report")
    public void jobReport(@Payload ApiTestCaseJobReport jobReport) throws Exception {
        jobService.handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/scene-job-report")
    public void sceneJobReport(@Payload SceneCaseJobReport jobReport) throws Exception {
        jobService.handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-job")
    public void runJob(@Payload ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) throws Exception {
        jobService.runJob(apiTestCaseJobRunRequest);
    }

    @PostMapping(Constants.SDK_VERSION + "/engine/bind")
    public String bind(@Validated @RequestBody EngineRegistrationRequest request) {
        return engineMemberManagement.bind(request);
    }
}
