package com.sms.satp.engine.task;

import com.sms.satp.common.listener.event.EngineInvalidEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EngineRecoveryDetectionTask {

    private final SuspiciousEngineManagement suspiciousEngineManagement;
    private final ApplicationEventPublisher applicationEventPublisher;

    public EngineRecoveryDetectionTask(SuspiciousEngineManagement suspiciousEngineManagement,
        ApplicationEventPublisher applicationEventPublisher) {
        this.suspiciousEngineManagement = suspiciousEngineManagement;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "* * * * * ?")
    public void increaseIndex() {
        Integer cursor = suspiciousEngineManagement.increaseIndex();
        List<String> engineIds = suspiciousEngineManagement.get(cursor);
        if (CollectionUtils.isNotEmpty(engineIds)) {
            applicationEventPublisher.publishEvent(new EngineInvalidEvent(engineIds));
        }
    }

}
