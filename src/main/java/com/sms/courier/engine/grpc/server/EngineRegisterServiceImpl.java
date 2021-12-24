package com.sms.courier.engine.grpc.server;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.engine.grpc.api.v1.EngineRegisterGrpc;
import com.sms.courier.engine.grpc.api.v1.GrpcEngineRegisterRequest;
import com.sms.courier.engine.grpc.api.v1.GrpcFunctionResponse;
import com.sms.courier.engine.grpc.api.v1.GrpcFunctionResponse.Builder;
import com.sms.courier.engine.listener.event.EngineRegisterEvent;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.repository.GlobalFunctionRepository;
import com.sms.courier.repository.ProjectFunctionRepository;
import com.sms.courier.utils.Assert;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;


@Slf4j
public class EngineRegisterServiceImpl extends EngineRegisterGrpc.EngineRegisterImplBase {

    private final ApplicationContext applicationContext;

    private final Map<String, StreamObserver<GrpcFunctionResponse>> streamObserverMap = new ConcurrentHashMap<>();

    public EngineRegisterServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(GrpcEngineRegisterRequest request,
        StreamObserver<GrpcFunctionResponse> responseObserver) {
        Builder functionBuilder = GrpcFunctionResponse.newBuilder().setOperationType(0);
        try {
            Assert.isTrue(request.getPort() > 0, "The port must gt 0!");
            Assert.isTrue(StringUtils.isNotBlank(request.getName()), "The name must not empty!");
            Assert.isTrue(StringUtils.isNotBlank(request.getVersion()), "The version must not empty!");
            applicationContext.publishEvent(new EngineRegisterEvent(request));
            GrpcMapper grpcMapper = applicationContext.getBean(GrpcMapper.class);
            ProjectFunctionRepository projectFunctionRepository = applicationContext
                .getBean(ProjectFunctionRepository.class);
            GlobalFunctionRepository globalFunctionRepository = applicationContext
                .getBean(GlobalFunctionRepository.class);
            // Response project function.
            projectFunctionRepository.findAllByRemovedIsFalse().map(grpcMapper::toGrpcFunction).forEach(function -> {
                responseObserver.onNext(functionBuilder.setFunction(function).build());
            });
            // Response global function.
            globalFunctionRepository.findAllByRemovedIsFalse().map(grpcMapper::toGrpcFunction).forEach(function -> {
                responseObserver.onNext(functionBuilder.setFunction(function).build());
            });
            streamObserverMap.put(request.getName(), responseObserver);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            responseObserver.onError(e);
        } catch (Exception e) {
            log.error("Register engine error!", e);
            responseObserver.onError(new ApiTestPlatformException("Register engine error!"));
        }
    }

    public void pushFunction(GrpcFunctionResponse functionResponse) {
        streamObserverMap.forEach((key, value) -> {
            try {
                value.onNext(functionResponse);
            } catch (StatusRuntimeException e) {
                log.error("The response stream cancelled!");
                if (Code.CANCELLED == e.getStatus().getCode()) {
                    streamObserverMap.remove(key);
                }
            }
        });
    }
}
