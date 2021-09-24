package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCHEDULE_CASE_SERVICE;
import static com.sms.courier.common.constant.Constants.SCHEDULE_SCENE_CASE_SERVICE;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.ApiTestCaseJobRunRequest;
import com.sms.courier.dto.request.ApiTestRequest;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.JobService;
import com.sms.courier.service.SceneCaseJobService;
import java.util.Map;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelController {

    private final EngineMemberManagement engineMemberManagement;
    private final Map<String, JobService> caseJobServiceMap;

    public ChannelController(EngineMemberManagement engineMemberManagement,
        Map<String, JobService> caseJobServiceMap) {
        this.engineMemberManagement = engineMemberManagement;
        this.caseJobServiceMap = caseJobServiceMap;
    }

    @MessageMapping(Constants.SDK_VERSION + "/api-test")
    public void apiTest(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestRequest apiTestRequest) {
        getApiTestCaseJobService().apiTest(apiTestRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-job")
    public void runJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload ApiTestCaseJobRunRequest apiTestCaseJobRunRequest) {
        getApiTestCaseJobService().runJob(apiTestCaseJobRunRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/run-scene-job")
    public void runSceneJob(@Header(StompHeaderAccessor.USER_HEADER) UsernamePasswordAuthenticationToken authentication,
        @Payload AddSceneCaseJobRequest addSceneCaseJobRequest) {
        getSceneCaseJobService().runJob(addSceneCaseJobRequest, (CustomUser) authentication.getPrincipal());
    }

    @MessageMapping(Constants.SDK_VERSION + "/job-report")
    public void jobReport(@Payload ApiTestCaseJobReport jobReport) {
//        caseJobServiceMap.get(jobReport.getJobType().getServiceName()).handleJobReport(jobReport);
        caseJobServiceMap.get(SCHEDULE_CASE_SERVICE).handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/scene-job-report")
    public void sceneJobReport(@Payload SceneCaseJobReport jobReport) {
//        caseJobServiceMap.get(jobReport.getJobType().getServiceName()).handleJobReport(jobReport);
        caseJobServiceMap.get(SCHEDULE_SCENE_CASE_SERVICE).handleJobReport(jobReport);
    }

    @MessageMapping(Constants.SDK_VERSION + "/case-record")
    public void caseRecord(@Payload CaseRecordRequest caseRecordRequest) {
        engineMemberManagement.caseRecord(caseRecordRequest);
    }

    @MessageMapping(Constants.SDK_VERSION + "/runningCaseJobAck")
    public void runningCaseJobAck(@Payload RunningJobAck runningJobAck) {
//        caseJobServiceMap.get(runningJobAck.getJobType().getServiceName()).runningJobAck(runningJobAck);
        caseJobServiceMap.get(SCHEDULE_CASE_SERVICE).runningJobAck(runningJobAck);
    }

    @MessageMapping(Constants.SDK_VERSION + "/runningSceneCaseJobAck")
    public void runningSceneCaseJobAck(@Payload RunningJobAck runningJobAck) {
//        caseJobServiceMap.get(runningJobAck.getJobType().getServiceName()).runningJobAck(runningJobAck);
        caseJobServiceMap.get(SCHEDULE_SCENE_CASE_SERVICE).runningJobAck(runningJobAck);
    }

    private ApiTestCaseJobService getApiTestCaseJobService() {
        return (ApiTestCaseJobService) caseJobServiceMap.get(Constants.CASE_SERVICE);
    }

    private SceneCaseJobService getSceneCaseJobService() {
        return (SceneCaseJobService) caseJobServiceMap.get(Constants.SCENE_CASE_SERVICE);
    }
}
