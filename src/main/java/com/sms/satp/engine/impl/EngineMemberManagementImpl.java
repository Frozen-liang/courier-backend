package com.sms.satp.engine.impl;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.CaseRecordRequest;
import com.sms.satp.engine.EngineId;
import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMember;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import com.sms.satp.utils.ExceptionUtils;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class EngineMemberManagementImpl implements EngineMemberManagement {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, EngineMember> engineMembers = new ConcurrentHashMap<>();

    @Override
    public String bind(EngineRegistrationRequest request) {
        EngineMember engineMember =
            EngineMember.builder().destination(EngineId.generate()).host(request.getHost())
                .version(request.getVersion())
                .build();
        engineMembers.put(engineMember.getDestination(), engineMember);
        log.info("The destination {} of the test engine is binding.", engineMember.getDestination());
        return engineMember.getDestination();
    }

    @Override
    public void updateMemberStatus(String destination, EngineStatus status) {
        log.info("The status of the test engine {} was ready to update.", destination);
        engineMembers.computeIfPresent(destination,
            (key, oldMember) -> {
                log.info("The status of the test engine {} has changed from {} to {}.", oldMember.getDestination(),
                    oldMember.getStatus(),
                    status);
                oldMember.setStatus(status);
                return oldMember;
            }
        );

    }

    @Override
    public String getAvailableMember() throws ApiTestPlatformException {
        List<String> availableMembers = engineMembers.values().stream()
            .filter(engineMember -> engineMember.getStatus().equals(EngineStatus.RUNNING))
            .map(EngineMember::getDestination)
            .collect(Collectors.toUnmodifiableList());
        if (CollectionUtils.isEmpty(availableMembers)) {
            throw ExceptionUtils.mpe("No engines are available.");
        }

        return availableMembers.get(random.nextInt(availableMembers.size()));
    }

    @Override
    public void caseRecord(CaseRecordRequest caseRecordRequest) {
        engineMembers.computeIfPresent(caseRecordRequest.getDestination(), (key, member) -> {
            member.setCaseTaskSize(caseRecordRequest.getCaseCount());
            member.setSceneCaseTaskSize(caseRecordRequest.getSceneCaseCount());
            member.setCurrentTaskSize(caseRecordRequest.getCaseCount() + caseRecordRequest.getSceneCaseCount());
            log.info("The destination {} currentTask {} caseTask {} sceneCaseTask {}.", member.getDestination(),
                member.getCurrentTaskSize(), member.getCaseTaskSize(), member.getSceneCaseTaskSize());
            return member;
        });
    }

    @Override
    public void unBind(String sessionId) {
        Optional<EngineMember> engineMemberOptional = engineMembers.values().stream()
            .filter(engineMember -> sessionId.equals(engineMember.getSessionId())).findFirst();
        engineMemberOptional.ifPresent(engineMember -> {
            engineMembers.remove(engineMember.getDestination());
            log.info("The destination {} unbind from member of the engine.", engineMember.getDestination());
        });
    }

    @Override
    public void active(EngineMember engineMember) {
        engineMembers.computeIfPresent(engineMember.getDestination(), (key, oldEngineMember) -> {
            oldEngineMember.setSessionId(engineMember.getSessionId());
            oldEngineMember.setStatus(EngineStatus.RUNNING);
            log.info("The test engine {} activated.", key);
            return oldEngineMember;
        });
    }


}
