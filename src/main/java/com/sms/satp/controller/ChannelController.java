package com.sms.satp.controller;

import static com.sms.satp.security.TokenType.ENGINE;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.dto.request.ApiTestCaseJobRunRequest;
import com.sms.satp.dto.request.ApiTestRequest;
import com.sms.satp.dto.request.CaseRecordRequest;
import com.sms.satp.dto.response.EngineRegistrationResponse;
import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import com.sms.satp.entity.job.ApiTestCaseJobReport;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.ApiTestCaseJobService;
import com.sms.satp.service.SceneCaseJobService;
import java.util.Collections;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelController {

    private final ApiTestCaseJobService apiTestCaseJobService;
    private final SceneCaseJobService sceneCaseJobService;
    private final EngineMemberManagement engineMemberManagement;
    private final JwtTokenManager jwtTokenManager;

    public ChannelController(ApiTestCaseJobService apiTestCaseJobService,
        SceneCaseJobService sceneCaseJobService, EngineMemberManagement engineMemberManagement,
        JwtTokenManager jwtTokenManager) {
        this.apiTestCaseJobService = apiTestCaseJobService;
        this.sceneCaseJobService = sceneCaseJobService;
        this.engineMemberManagement = engineMemberManagement;
        this.jwtTokenManager = jwtTokenManager;
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

    @PostMapping(Constants.SDK_VERSION + "/engine/bind")
    public EngineRegistrationResponse bind(@Validated @RequestBody EngineRegistrationRequest request) {
        String destination = engineMemberManagement.bind(request);
        CustomUser engine = new CustomUser("engine", "", Collections.emptyList(), destination, "",
            ENGINE);
        return EngineRegistrationResponse.builder().subscribeAddress(destination)
            .token(jwtTokenManager.generateAccessToken(engine))
            .build();
    }
}
