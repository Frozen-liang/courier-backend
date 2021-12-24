package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.JobServiceFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelController {

    private final JobServiceFactory jobServiceFactory;

    public ChannelController(JobServiceFactory jobServiceFactory) {
        this.jobServiceFactory = jobServiceFactory;
    }

    @MessageMapping(Constants.SDK_VERSION + "/api-test")
    public void apiTest(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestRequest apiTestRequest) {
        jobServiceFactory.getApiTestCaseJobService()
            .apiTest(apiTestRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-job")
    public void runJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        jobServiceFactory.getApiTestCaseJobService().runJob(apiTestCaseJobRunRequest,
            (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-scene-job")
    public void runSceneJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload AddSceneCaseJobRequest addSceneCaseJobRequest) {
        jobServiceFactory.getSceneCaseJobService()
            .runJob(addSceneCaseJobRequest, (CustomUser) authentication.getPrincipal());
    }
}
