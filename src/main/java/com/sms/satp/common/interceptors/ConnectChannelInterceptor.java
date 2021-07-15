package com.sms.satp.common.interceptors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.utils.SecurityUtil;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConnectChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public ConnectChannelInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
            .getAccessor(message, StompHeaderAccessor.class);
        if (Objects.nonNull(accessor) && CONNECT.equals(accessor.getCommand())) {
            List<String> nativeHeader = accessor.getNativeHeader(AUTHORIZATION);
            if (CollectionUtils.isNotEmpty(nativeHeader)) {
                String header = nativeHeader.get(0);
                if (StringUtils.isNotBlank(header) || header.startsWith("Bearer ")) {
                    String token = header.split(" ")[1].trim();
                    if (jwtTokenManager.validate(token)) {
                        String email = jwtTokenManager.getEmail(token);
                        String userId = jwtTokenManager.getUserId(token);
                        String username = jwtTokenManager.getUsername(token);
                        Authentication authentication = SecurityUtil
                            .newAuthentication(userId, email, username, 1L, Collections.emptyList());
                        accessor.setUser(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return message;
                    }
                }
            }
            return message;
        }
        return message;
    }
}
