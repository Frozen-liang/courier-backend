package com.sms.courier.engine.grpc.client;

import com.sms.courier.engine.grpc.api.v1.EngineGrpc;
import com.sms.courier.engine.grpc.api.v1.EngineGrpc.EngineStub;
import com.sms.courier.engine.grpc.loadbalancer.Constants;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class EngineStubFactory  {

    private static ManagedChannel channel = createChannel();

    private static EngineStub engineStub = createEngineStub();

    private EngineStubFactory() {
    }

    public static EngineStub getEngineStub() {
        return engineStub;
    }

    public static void refresh() {
        channel.shutdown();
        channel = createChannel();
        engineStub = createEngineStub();
    }

    private static ManagedChannel createChannel() {
        return ManagedChannelBuilder.forTarget("engine")
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .compressorRegistry(CompressorRegistry.getDefaultInstance())
            .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
            .defaultLoadBalancingPolicy(Constants.ROUND_ROBIN)
            .build();
    }

    private static EngineStub createEngineStub() {
        if (channel == null) {
            channel = createChannel();
        }
        return EngineGrpc.newStub(channel);
    }
}
