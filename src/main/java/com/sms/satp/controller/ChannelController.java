package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.CaseRecordRequest;
import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.service.ApiTestCaseJobService;
import com.sms.satp.service.SceneCaseJobService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelController {

    private final ApiTestCaseJobService apiTestCaseJobService;
    private final SceneCaseJobService sceneCaseJobService;
    private final EngineMemberManagement engineMemberManagement;

    public ChannelController(ApiTestCaseJobService apiTestCaseJobService,
        SceneCaseJobService sceneCaseJobService, EngineMemberManagement engineMemberManagement) {
        this.apiTestCaseJobService = apiTestCaseJobService;
        this.sceneCaseJobService = sceneCaseJobService;
        this.engineMemberManagement = engineMemberManagement;
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-job")
    public void runJob(@Payload ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest);
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-scene-job")
    public void runSceneJob(@Payload AddSceneCaseJobRequest addSceneCaseJobRequest) {
        sceneCaseJobService.runJob(addSceneCaseJobRequest);
    }

    @MessageMapping(Constants.SDK_VERSION + "/job-report")
    public void jobReport(@Payload ApiTestCaseJobReport jobReport) {
        apiTestCaseJobService.handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/scene-job-report")
    public void sceneJobReport(@Payload SceneCaseJobReport jobReport) {
        sceneCaseJobService.handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/case-record")
    public void caseRecord(@Payload CaseRecordRequest caseRecordRequest) {
        engineMemberManagement.caseRecord(caseRecordRequest);
    }

    @PostMapping(Constants.SDK_VERSION + "/engine/bind")
    public String bind(@Validated @RequestBody EngineRegistrationRequest request) {
        return engineMemberManagement.bind(request);
    }
}
