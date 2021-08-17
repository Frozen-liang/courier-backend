package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.SceneCaseJobService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @MessageMapping(Constants.SDK_VERSION + "/api-test")
    public void apiTest(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestRequest apiTestRequest) {
        apiTestCaseJobService.apiTest(apiTestRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-job")
    public void runJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        apiTestCaseJobService.runJob(apiTestCaseJobRunRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-scene-job")
    public void runSceneJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload AddSceneCaseJobRequest addSceneCaseJobRequest) {
        sceneCaseJobService.runJob(addSceneCaseJobRequest, (CustomUser) authentication.getPrincipal());
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

}
