package com.sms.courier.config;

import com.sms.courier.common.interceptors.ConnectChannelInterceptor;
import com.sms.courier.common.interceptors.DataCompressionChannelInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

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
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/user").setAllowedOriginPatterns("*")
            .withSockJS();

        registry.addEndpoint("/engine").setAllowedOriginPatterns("*")
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setUserDestinationPrefix("/engine");
        config.setApplicationDestinationPrefixes("/channel");
        config.enableSimpleBroker("/engine", "/user");
    }
}
