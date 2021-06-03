package com.sms.satp.engine.service.impl;

import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.model.CaseJob;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.job.ApiTestCaseJob;
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
    public void dispatch(CaseJob caseJob) {
        Set<String> availableMembers = engineMemberManagement.getAvailableMembers();
        availableMembers.stream().findAny().ifPresent((destination) -> {
            log.info("Run case job. destination {}", destination);
            simpMessagingTemplate.convertAndSend(destination, caseJob);
        });
    }

    @Override
    public void sendJobReport(String destination, ApiTestCaseJob caseJob) {
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseJob);
    }

    @Override
    public void sendJobReport(String destination, CaseJob caseJob) {
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, caseJob);
    }
}
