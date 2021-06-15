package com.sms.satp.engine.service.impl;

import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.common.CaseReport;
import java.util.List;
import java.util.Set;
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
    public void dispatch(ApiTestCaseJob caseJob) {
        Set<String> availableMembers = engineMemberManagement.getAvailableMembers();
        availableMembers.stream().findAny().ifPresent((destination) -> {
            log.info("Send ApiTestCaseJob. destination {}", destination);
            simpMessagingTemplate.convertAndSend(destination, caseJob);
        });
    }

    @Override
    public void dispatch(SceneCaseJob caseJob) {
        Set<String> availableMembers = engineMemberManagement.getAvailableMembers();
        availableMembers.stream().findAny().ifPresent((destination) -> {
            log.info("Run case job. destination {}", destination);
            simpMessagingTemplate.convertAndSend(destination, caseJob);
        });
    }

    @Override
    public void sendJobReport(String destination, CaseReport caseReport) {
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseReport);
    }

    @Override
    public void sendJobReport(String destination, List<CaseReport> caseReport) {
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseReport);
    }

    @Override
    public void sendMessage(String destination, String message) {
        log.info("Send message {} to {}", message, destination);
        simpMessagingTemplate.convertAndSend(destination, message);
    }
}
