package com.sms.courier.engine.grpc.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.engine.grpc.api.v1.GrpcEngineRegisterRequest;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.mapper.GrpcMapperImpl;
import com.sms.courier.repository.GlobalFunctionRepository;
import com.sms.courier.repository.ProjectFunctionRepository;
import io.grpc.stub.StreamObserver;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for EngineRegisterServiceImpl")
public class EngineRegisterServiceImplTest {

    private final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final GrpcMapper grpcMapper = new GrpcMapperImpl();
    private final EngineRegisterServiceImpl engineRegisterService = new EngineRegisterServiceImpl(applicationContext);

    @Test
    @DisplayName("Test for openEngine in EngineMemberManagement")
    public void register_test() {
        GrpcEngineRegisterRequest request =
            GrpcEngineRegisterRequest.newBuilder().setPort(50051).setName("name").setVersion("1.0").build();
        when(applicationContext.getBean(GrpcMapper.class)).thenReturn(grpcMapper);
        ProjectFunctionRepository projectFunctionRepository = mock(ProjectFunctionRepository.class);
        GlobalFunctionRepository globalFunctionRepository = mock(GlobalFunctionRepository.class);
        when(applicationContext.getBean(ProjectFunctionRepository.class)).thenReturn(projectFunctionRepository);
        when(applicationContext.getBean(GlobalFunctionRepository.class)).thenReturn(globalFunctionRepository);
        when(projectFunctionRepository.findAllByRemovedIsFalse())
            .thenReturn(Stream.of(ProjectFunctionEntity.builder().build()));
        when(globalFunctionRepository.findAllByRemovedIsFalse())
            .thenReturn(Stream.of(GlobalFunctionEntity.builder().build()));
        StreamObserver streamObserver = mock(StreamObserver.class);
        doNothing().when(streamObserver).onNext(any());
        engineRegisterService.register(request, streamObserver);
        verify(streamObserver, times(2)).onNext(any());
    }
}
