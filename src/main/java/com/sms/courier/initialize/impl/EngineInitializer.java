package com.sms.courier.initialize.impl;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.grpc.GrpcProperties;
import com.sms.courier.engine.grpc.interceptor.AuthorizationServerInterceptor;
import com.sms.courier.engine.grpc.server.EngineRegisterServiceImpl;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.utils.ApplicationContextUtils;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EngineInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        ApplicationContextUtils.INSTANCE.setApplicationContext(applicationContext);
        EngineRegisterServiceImpl engineRegisterService = new EngineRegisterServiceImpl(applicationContext);
        GrpcProperties grpcProperties = applicationContext.getBean(GrpcProperties.class);
        AuthorizationServerInterceptor interceptor = applicationContext.getBean(AuthorizationServerInterceptor.class);
        initServer(engineRegisterService, grpcProperties, interceptor);
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        genericApplicationContext.registerBean(EngineRegisterServiceImpl.class, () -> engineRegisterService);
        log.info("Clean running engine");
        EngineMemberRepository engineMemberRepository = applicationContext.getBean(EngineMemberRepository.class);
        engineMemberRepository.findAllByContainerStatus(ContainerStatus.START)
            .forEach(engine -> {
                engine.setContainerStatus(ContainerStatus.DESTROY);
                engine.setStatus(EngineStatus.INVALID);
                engineMemberRepository.save(engine);
            });
    }

    @SneakyThrows
    private void initServer(EngineRegisterServiceImpl engineRegisterService, GrpcProperties grpcProperties,
        AuthorizationServerInterceptor interceptor) {
        Server server = ServerBuilder.forPort(grpcProperties.getPort())
            .addService(engineRegisterService)
            .intercept(interceptor)
            .build()
            .start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            log.info("*** shutting down gRPC server since JVM is shutting down!");
            try {
                if (Objects.nonNull(server)) {
                    server.shutdown();
                }
            } catch (Exception e) {
                log.error("Shutting grpc service error!");
            }
            log.info("*** grpc server shut down!");
        }));
    }

    @Override
    public int getOrder() {
        return Order.ENGINE;
    }
}
