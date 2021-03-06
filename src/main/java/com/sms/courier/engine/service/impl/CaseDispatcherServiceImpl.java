package com.sms.courier.engine.service.impl;

import static com.sms.courier.utils.UserDestinationUtil.getCaseDest;
import static com.sms.courier.utils.UserDestinationUtil.getSceneCaseDest;

import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.websocket.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseDispatcherServiceImpl implements CaseDispatcherService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public CaseDispatcherServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
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
    public void sendCaseErrorMessage(String userId, String message) {
        String destination = getCaseDest(userId);
        log.info("Send message {} to {}", message, getCaseDest(userId));
        simpMessagingTemplate.convertAndSend(destination, Payload.fail(message));
    }

    @Override
    public void sendSceneCaseErrorMessage(String userId, String message) {
        String destination = getSceneCaseDest(userId);
        log.info("Send message {} to {}", message, getSceneCaseDest(userId));
        simpMessagingTemplate.convertAndSend(destination, Payload.fail(message));
    }

}
