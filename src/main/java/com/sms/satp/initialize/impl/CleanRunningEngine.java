package com.sms.satp.initialize.impl;

import static com.sms.satp.engine.enums.EngineStatus.RUNNING;

import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMemberEntity;
import com.sms.satp.initialize.DataInitializer;
import com.sms.satp.repository.EngineMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CleanRunningEngine implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        EngineMemberRepository engineMemberRepository = applicationContext.getBean(EngineMemberRepository.class);
        List<EngineMemberEntity> engineMembers = engineMemberRepository.findAllByStatus(RUNNING)
            .collect(Collectors.toList());
        engineMembers.forEach(engine -> engine.setStatus(EngineStatus.INVALID));
        log.debug("Clean running engine");
        engineMemberRepository.saveAll(engineMembers);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
