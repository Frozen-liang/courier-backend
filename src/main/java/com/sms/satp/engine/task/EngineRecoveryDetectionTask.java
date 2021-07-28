package com.sms.satp.engine.task;

import static com.sms.satp.engine.enums.EngineStatus.INVALID;

import com.sms.satp.engine.model.EngineMemberEntity;
import com.sms.satp.repository.EngineMemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EngineRecoveryDetectionTask {

    private final SuspiciousEngineManagement suspiciousEngineManagement;
    private final EngineMemberRepository engineMemberRepository;

    public EngineRecoveryDetectionTask(SuspiciousEngineManagement suspiciousEngineManagement,
        EngineMemberRepository engineMemberRepository) {
        this.suspiciousEngineManagement = suspiciousEngineManagement;
        this.engineMemberRepository = engineMemberRepository;
    }

    @Scheduled(cron = "* * * * * ?")
    @Async("engineDetection")
    public void detection() {
        Integer currentIndex = suspiciousEngineManagement.getCurrentIndex();
        suspiciousEngineManagement.increaseIndex();
        List<String> engineIds = suspiciousEngineManagement.get(currentIndex);
        Optional.ofNullable(engineIds).ifPresent(this::updateEngineStatus);
        // TODO publish event
    }

    private void updateEngineStatus(List<String> engineIds) {
        Iterable<EngineMemberEntity> engineMemberEntities = engineMemberRepository.findAllById(engineIds);
        engineMemberEntities.forEach(entity -> entity.setStatus(INVALID));
        engineMemberRepository.saveAll(engineMemberEntities);
    }

}
