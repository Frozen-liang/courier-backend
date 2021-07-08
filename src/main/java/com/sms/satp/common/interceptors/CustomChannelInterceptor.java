package com.sms.satp.common.interceptors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.utils.SecurityUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public CustomChannelInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
            .getAccessor(message, StompHeaderAccessor.class);
        if (Objects.nonNull(accessor) && CONNECT.equals(accessor.getCommand())) {
            String token = (String) accessor.getHeader(AUTHORIZATION);
            log.info("加入连接 token:{}", token);
            if (jwtTokenManager.validate(token)) {
                accessor.setHeader("userId", jwtTokenManager.getUserId(token));
            }
        }
        return message;
    }
}
