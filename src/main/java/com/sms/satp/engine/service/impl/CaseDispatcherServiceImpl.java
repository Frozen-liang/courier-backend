package com.sms.satp.engine.service.impl;

import static com.sms.satp.utils.UserDestinationUtil.getCaseDest;
import static com.sms.satp.utils.UserDestinationUtil.getSceneCaseDest;

import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.common.CaseReport;
import com.sms.satp.repository.ApiTestCaseJobRepository;
import com.sms.satp.repository.SceneCaseJobRepository;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.websocket.Payload;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class CaseDispatcherServiceImpl implements CaseDispatcherService {

    private final EngineMemberManagement engineMemberManagement;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ApiTestCaseJobRepository apiTestCaseJobRepository;
    private final SceneCaseJobRepository sceneCaseJobRepository;

    public CaseDispatcherServiceImpl(EngineMemberManagement engineMemberManagement,
        SimpMessagingTemplate simpMessagingTemplate,
        ApiTestCaseJobRepository apiTestCaseJobRepository,
        SceneCaseJobRepository sceneCaseJobRepository) {
        this.engineMemberManagement = engineMemberManagement;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.apiTestCaseJobRepository = apiTestCaseJobRepository;
        this.sceneCaseJobRepository = sceneCaseJobRepository;
    }

    @Override
    public void dispatch(ApiTestCaseJob caseJob) {
        Set<String> availableMembers = engineMemberManagement.getAvailableMembers();
        if (CollectionUtils.isEmpty(availableMembers)) {
            apiTestCaseJobRepository.deleteById(caseJob.getId());
            throw ExceptionUtils.mpe("No engines are available.");
        }
        availableMembers.stream().findAny().ifPresent((destination) -> {
            log.info("Send ApiTestCaseJob. destination {}", destination);
            simpMessagingTemplate.convertAndSend(destination, caseJob);
        });
    }

    @Override
    public void dispatch(SceneCaseJob caseJob) {
        Set<String> availableMembers = engineMemberManagement.getAvailableMembers();
        if (CollectionUtils.isEmpty(availableMembers)) {
            sceneCaseJobRepository.deleteById(caseJob.getId());
            throw ExceptionUtils.mpe("No engines are available.");
        }
        availableMembers.stream().findAny().ifPresent((destination) -> {
            log.info("Run case job. destination {}", destination);
            simpMessagingTemplate.convertAndSend(destination, caseJob);
        });
    }

    @Override
    public void sendJobReport(String userId, CaseReport caseReport) {
        String destination = getCaseDest(userId);
        log.info("Send job report. destination {}", destination);
        simpMessagingTemplate.convertAndSend(destination, Payload.ok(caseReport));
    }

    @Override
    public void sendJobReport(String userId, List<CaseReport> caseReport) {
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
