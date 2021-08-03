package com.sms.courier.engine.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.SceneCaseJobService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for EngineRecoveryDetectionTask")
public class EngineRecoveryDetectionTaskTest {

    private final Integer currentIndex = 1;
    private final List<String> engineIds = List.of("engine/123/invoke");
    private final SuspiciousEngineManagement suspiciousEngineManagement = mock(SuspiciousEngineManagement.class);
    private final EngineMemberRepository engineMemberRepository = mock(EngineMemberRepository.class);
    private final ApiTestCaseJobService apiTestCaseJobService = mock(ApiTestCaseJobService.class);
    private final SceneCaseJobService sceneCaseJobService = mock(SceneCaseJobService.class);
    private final EngineRecoveryDetectionTask engineRecoveryDetectionTask =
        new EngineRecoveryDetectionTask(suspiciousEngineManagement, engineMemberRepository, apiTestCaseJobService,
            sceneCaseJobService);

    @Test
    @DisplayName("Test for increaseIndex in EngineRecoveryDetectionTask")
    public void increaseIndex_test() {
        when(suspiciousEngineManagement.increaseIndex()).thenReturn(currentIndex + 1);
        engineRecoveryDetectionTask.increaseIndex();
        verify(suspiciousEngineManagement, times(1)).increaseIndex();
    }

    @Test
    @DisplayName("Test for engineDetection in EngineRecoveryDetectionTask")
    public void engineDetection_test() {
        when(suspiciousEngineManagement.getCurrentIndex()).thenReturn(currentIndex);
        when(suspiciousEngineManagement.get(currentIndex)).thenReturn(engineIds);
        when(engineMemberRepository.findAllByDestinationIn(engineIds))
            .thenReturn(List.of(EngineMemberEntity.builder().build()));
        when(engineMemberRepository.saveAll(any())).thenReturn(null);
        doNothing().when(apiTestCaseJobService).reallocateJob(engineIds);
        doNothing().when(sceneCaseJobService).reallocateJob(engineIds);
        engineRecoveryDetectionTask.engineDetection();
        verify(apiTestCaseJobService, times(1)).reallocateJob(engineIds);
        verify(sceneCaseJobService, times(1)).reallocateJob(engineIds);
    }
}
