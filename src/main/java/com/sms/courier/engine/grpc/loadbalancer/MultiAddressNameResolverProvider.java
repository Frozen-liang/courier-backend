package com.sms.courier.engine.grpc.loadbalancer;

import com.google.common.base.Stopwatch;
import io.grpc.InternalServiceProviders;
import io.grpc.NameResolver;
import io.grpc.NameResolver.Args;
import io.grpc.NameResolverProvider;
import io.grpc.internal.GrpcUtil;
import java.net.URI;

public class MultiAddressNameResolverProvider extends NameResolverProvider {

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, Args args) {
        return new EngineNameResolver("courierAuthority", args,
            GrpcUtil.SHARED_CHANNEL_EXECUTOR,
            Stopwatch.createUnstarted(),
            InternalServiceProviders.isAndroid(getClass().getClassLoader()));
    }

    @Override
    public String getDefaultScheme() {
        return "courier";
    }

}
