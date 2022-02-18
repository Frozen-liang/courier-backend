package com.sms.courier.engine.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.grpc.api.v1.EngineGrpc.EngineStub;
import com.sms.courier.engine.grpc.client.EngineStubFactory;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.mapper.GrpcMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("Test for EngineJobManagementImpl")
public class EngineJobManagementImplTest {

    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final GrpcMapper grpcMapper = new GrpcMapperImpl();
    private final EngineJobManagement engineJobManagement = new EngineJobManagementImpl(applicationEventPublisher,
        grpcMapper);
    private static final EngineStub ENGINE_STUB = mock(EngineStub.class);

    private final static MockedStatic<EngineStubFactory> MOCKED_STATIC = mockStatic(EngineStubFactory.class);

    static {
        MOCKED_STATIC.when(EngineStubFactory::getEngineStub).thenReturn(ENGINE_STUB);
    }

    @AfterAll
    public static void close() {
        MOCKED_STATIC.close();
    }

    @DisplayName("Test for dispatcherCaseJob in EngineJobManagementImpl")
    @Test

    public void dispatcherCaseJob_test() {
        ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity.builder().id("1").build();
        ScheduleCaseJobEntity scheduleCaseJobEntity = ScheduleCaseJobEntity.builder().id("id").build();
        when(ENGINE_STUB.withOption(any(), any())).thenReturn(ENGINE_STUB);
        when(ENGINE_STUB.withInterceptors(any())).thenReturn(ENGINE_STUB);
        engineJobManagement.dispatcherJob(apiTestCaseJobEntity);
        engineJobManagement.dispatcherJob(scheduleCaseJobEntity);
        verify(ENGINE_STUB, times(2)).sendCaseJob(any(), any());
    }

    @DisplayName("Test for dispatcherSceneCaseJob in EngineJobManagementImpl")
    @Test
    public void dispatcherSceneCaseJob_test() {
        SceneCaseJobEntity sceneCaseJobEntity = SceneCaseJobEntity.builder().id("1").build();
        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity = ScheduleSceneCaseJobEntity.builder().id("1").build();
        when(ENGINE_STUB.withOption(any(), any())).thenReturn(ENGINE_STUB);
        when(ENGINE_STUB.withInterceptors(any())).thenReturn(ENGINE_STUB);
        engineJobManagement.dispatcherJob(sceneCaseJobEntity);
        engineJobManagement.dispatcherJob(scheduleSceneCaseJobEntity);
        verify(ENGINE_STUB, times(2)).sendSceneCaseJob(any(), any());
    }

}
