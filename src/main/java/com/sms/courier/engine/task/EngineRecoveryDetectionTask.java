package com.sms.courier.engine.task;

import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.SceneCaseJobService;
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
    private final ApiTestCaseJobService apiTestCaseJobService;
    private final SceneCaseJobService sceneCaseJobService;

    public EngineRecoveryDetectionTask(SuspiciousEngineManagement suspiciousEngineManagement,
        EngineMemberRepository engineMemberRepository, ApiTestCaseJobService apiTestCaseJobService,
        SceneCaseJobService sceneCaseJobService) {
        this.suspiciousEngineManagement = suspiciousEngineManagement;
        this.engineMemberRepository = engineMemberRepository;
        this.apiTestCaseJobService = apiTestCaseJobService;
        this.sceneCaseJobService = sceneCaseJobService;
    }

    @Scheduled(cron = "* * * * * ?")
    public void increaseIndex() {
        suspiciousEngineManagement.increaseIndex();
    }

    @Scheduled(cron = "* * * * * ?")
    @Async("engineDetection")
    public void engineDetection() {
        Integer currentIndex = suspiciousEngineManagement.getCurrentIndex();
        List<String> engineIds = suspiciousEngineManagement.get(currentIndex);
        Optional.ofNullable(engineIds).ifPresent((engineId) -> {
            updateEngineStatus(engineIds);
            // Reallocate api test job to engine.
            apiTestCaseJobService.reallocateJob(engineIds);
            // Reallocate scene job to engine.
            sceneCaseJobService.reallocateJob(engineIds);
        });
    }

    private void updateEngineStatus(List<String> engineIds) {
        List<EngineMemberEntity> engineMemberEntities = engineMemberRepository.findAllByDestinationIn(engineIds);
        engineMemberEntities.forEach(entity -> entity.setStatus(EngineStatus.INVALID));
        engineMemberRepository.saveAll(engineMemberEntities);
    }

}
