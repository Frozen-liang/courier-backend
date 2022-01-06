package com.sms.courier.engine.grpc.interceptor;

import static com.sms.courier.common.constant.Constants.BEARER;
import static com.sms.courier.engine.grpc.loadbalancer.Constants.AUTHORIZATION;

import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.utils.Assert;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationServerInterceptor implements ServerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public AuthorizationServerInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {
        try {
            String value = headers.get(AUTHORIZATION);
            Assert.notEmpty(value, "The authorization token is missing!");
            Assert.isTrue(value.startsWith(BEARER), "Unknown authorization type!");
            String token = value.split(" ")[1];
            Assert.isTrue(jwtTokenManager.validate(token), "Invalid JWT token!");
            return next.startCall(call, headers);
        } catch (Exception e) {
            log.error("Engine grpc authorization error!");
            call.close(Status.UNAUTHENTICATED.withDescription(e.getMessage()), headers);
        }
        return new ServerCall.Listener<>() {

        };
    }
}
