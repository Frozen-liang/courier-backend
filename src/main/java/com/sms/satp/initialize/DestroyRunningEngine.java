package com.sms.satp.initialize;

import static com.sms.satp.engine.enums.EngineStatus.RUNNING;

import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMemberEntity;
import com.sms.satp.repository.EngineMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class DestroyRunningEngine implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        EngineMemberRepository engineMemberRepository = applicationContext.getBean(EngineMemberRepository.class);
        List<EngineMemberEntity> engineMembers = engineMemberRepository.findAllByStatus(RUNNING)
            .collect(Collectors.toList());
        engineMembers.forEach(engine -> engine.setStatus(EngineStatus.INVALID));
        log.info("Destroy engine");
        engineMemberRepository.saveAll(engineMembers);
    }
}
