package com.sms.satp.initialize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMemberEntity;
import com.sms.satp.repository.EngineMemberRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Tests for DestroyRunningEngineTest")
public class DestroyRunningEngineTest {

    private final DestroyRunningEngine destroyRunningEngine = new DestroyRunningEngine();
    private final ApplicationStartedEvent applicationStartedEvent = mock(ApplicationStartedEvent.class);
    private ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final EngineMemberRepository engineMemberRepository = mock(EngineMemberRepository.class);
    private final EngineMemberEntity engineMemberEntity = EngineMemberEntity.builder().build();

    @Test
    @DisplayName("Test the onApplicationEvent_test method in the DestroyRunningEngine")
    public void onApplicationEvent_test(){
        when(applicationStartedEvent.getApplicationContext()).thenReturn(applicationContext);
        when(applicationContext.getBean(EngineMemberRepository.class)).thenReturn(engineMemberRepository);
        when(engineMemberRepository.findAllByStatus(EngineStatus.RUNNING)).thenReturn(Stream.of(engineMemberEntity));
        destroyRunningEngine.onApplicationEvent(applicationStartedEvent);
        verify(engineMemberRepository).saveAll(any());
    }
}
