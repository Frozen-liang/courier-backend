package com.sms.courier.engine.listener.event;

import com.sms.courier.engine.grpc.api.v1.GrpcEngineRegisterRequest;
import lombok.Getter;

@Getter
public class EngineRegisterEvent {

    private final GrpcEngineRegisterRequest request;

    public EngineRegisterEvent(GrpcEngineRegisterRequest request) {
        this.request = request;
    }
}
