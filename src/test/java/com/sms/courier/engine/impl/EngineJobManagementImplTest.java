package com.sms.courier.engine.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.grpc.api.v1.EngineGrpc.EngineStub;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.mapper.GrpcMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("Test for EngineJobManagementImpl")
public class EngineJobManagementImplTest {

    private final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final EngineStub engineStub = mock(EngineStub.class);
    private final GrpcMapper grpcMapper = new GrpcMapperImpl();
    private final EngineJobManagement engineJobManagement = new EngineJobManagementImpl(applicationContext,
        applicationEventPublisher, grpcMapper);

    @DisplayName("Test for dispatcherCaseJob in EngineJobManagementImpl")
    @Test
    public void dispatcherCaseJob_test() {
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity.builder().id("1").build();
        ScheduleCaseJobEntity scheduleCaseJobEntity = ScheduleCaseJobEntity.builder().id("id").build();
        when(applicationContext.getBean(EngineStub.class)).thenReturn(engineStub);
        when(engineStub.withOption(any(), any())).thenReturn(engineStub);
        when(engineStub.withInterceptors(any())).thenReturn(engineStub);
        engineJobManagement.dispatcherJob(apiTestCaseJobEntity);
        engineJobManagement.dispatcherJob(scheduleCaseJobEntity);
        verify(engineStub, times(2)).sendCaseJob(any(), any());
    }

    @DisplayName("Test for dispatcherSceneCaseJob in EngineJobManagementImpl")
    @Test
    public void dispatcherSceneCaseJob_test() {
        SceneCaseJobEntity sceneCaseJobEntity = SceneCaseJobEntity.builder().id("1").build();
        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity = ScheduleSceneCaseJobEntity.builder().id("1").build();
        when(applicationContext.getBean(EngineStub.class)).thenReturn(engineStub);
        when(engineStub.withOption(any(), any())).thenReturn(engineStub);
        when(engineStub.withInterceptors(any())).thenReturn(engineStub);
        engineJobManagement.dispatcherJob(sceneCaseJobEntity);
        engineJobManagement.dispatcherJob(scheduleSceneCaseJobEntity);
        verify(engineStub, times(2)).sendSceneCaseJob(any(), any());
    }

}
