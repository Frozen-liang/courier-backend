package com.sms.courier.common.listener;

import static com.sms.courier.engine.enums.EngineStatus.INVALID;

import com.sms.courier.common.listener.event.EngineInvalidEvent;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.service.JobService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EngineInvalidListener {

    private final EngineMemberRepository engineMemberRepository;
    private final Map<String, JobService> jobServiceMap;


    public EngineInvalidListener(EngineMemberRepository engineMemberRepository,
        Map<String, JobService> jobServiceMap) {
        this.engineMemberRepository = engineMemberRepository;
        this.jobServiceMap = jobServiceMap;
    }


    @EventListener
    @Async
    public void doProcess(EngineInvalidEvent event) {
        List<String> engineIds = event.getEngineIds();
        if (CollectionUtils.isEmpty(engineIds)) {
            return;
        }
        updateEngineStatus(engineIds);
        // Reallocate job to engine.
        jobServiceMap.values().forEach(jobService -> jobService.reallocateJob(engineIds));
    }

    private void updateEngineStatus(List<String> engineIds) {
        List<EngineMemberEntity> engineMemberEntities = engineMemberRepository.findAllByDestinationIn(engineIds);
        engineMemberEntities.forEach(entity -> entity.setStatus(INVALID));
        engineMemberRepository.saveAll(engineMemberEntities);
    }
}
