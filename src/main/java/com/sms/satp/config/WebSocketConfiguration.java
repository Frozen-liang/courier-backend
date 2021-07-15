package com.sms.satp.config;

import com.sms.satp.common.interceptors.ConnectChannelInterceptor;
import com.sms.satp.common.interceptors.DataCompressionChannelInterceptor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final ConnectChannelInterceptor connectChannelInterceptor;
    private final DataCompressionChannelInterceptor dataCompressionChannelInterceptor;

    public WebSocketConfiguration(ConnectChannelInterceptor connectChannelInterceptor,
        DataCompressionChannelInterceptor dataCompressionChannelInterceptor) {
        this.connectChannelInterceptor = connectChannelInterceptor;
        this.dataCompressionChannelInterceptor = dataCompressionChannelInterceptor;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(65536 * 10);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(connectChannelInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(dataCompressionChannelInterceptor);
    }

    @Override
    @SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/user").setAllowedOriginPatterns("*")
            .withSockJS();

        registry.addEndpoint("/engine").addInterceptors(new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                log.info("接入连接");
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                WebSocketHandler wsHandler, Exception exception) {

            }
        }).setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setUserDestinationPrefix("/engine");
        config.setApplicationDestinationPrefixes("/channel");
        config.enableSimpleBroker("/engine", "/user");
    }
}
