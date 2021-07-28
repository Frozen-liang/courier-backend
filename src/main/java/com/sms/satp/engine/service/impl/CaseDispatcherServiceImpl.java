package com.sms.satp.engine.service.impl;

import static com.sms.satp.utils.UserDestinationUtil.getCaseDest;
import static com.sms.satp.utils.UserDestinationUtil.getSceneCaseDest;

import com.sms.satp.dto.response.ApiTestCaseJobReportResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.SceneCaseJobReportResponse;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.websocket.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseDispatcherServiceImpl implements CaseDispatcherService {

    private final EngineMemberManagement engineMemberManagement;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public CaseDispatcherServiceImpl(EngineMemberManagement engineMemberManagement,
        SimpMessagingTemplate simpMessagingTemplate) {
        this.engineMemberManagement = engineMemberManagement;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public String dispatch(ApiTestCaseJobResponse caseJob) {
        String destination = engineMemberManagement.getAvailableMember();
        log.info("Send ApiTestCaseJob. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseJob);
        return destination;
    }

    @Override
    public String dispatch(SceneCaseJobResponse caseJob) {
        String destination = engineMemberManagement.getAvailableMember();
        log.info("Run case job. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseJob);
        return destination;
    }

    @Override
    public void sendJobReport(String userId, ApiTestCaseJobReportResponse caseReport) {
        String destination = getCaseDest(userId);
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, Payload.ok(caseReport));
    }

    @Override
    public void sendJobReport(String userId, SceneCaseJobReportResponse caseReport) {
        String destination = getSceneCaseDest(userId);
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, Payload.ok(caseReport));
    }

    @Override
    public void sendErrorMessage(String userId, String message) {
        String destination = getCaseDest(userId);
        log.info("Send message {} to {}", message, getCaseDest(userId));
        simpMessagingTemplate.convertAndSend(destination, Payload.fail(message));
    }
}
