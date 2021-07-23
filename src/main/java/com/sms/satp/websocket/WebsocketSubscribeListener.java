package com.sms.satp.websocket;

import com.sms.satp.engine.EngineMemberManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Slf4j
@Component
public class WebsocketSubscribeListener {


    private final EngineMemberManagement engineMemberManagement;

    public WebsocketSubscribeListener(EngineMemberManagement engineMemberManagement) {
        this.engineMemberManagement = engineMemberManagement;
    }


    @EventListener
    public void subscribe(SessionSubscribeEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        String destination = headers.get(SimpMessageHeaderAccessor.DESTINATION_HEADER, String.class);
        String sessionId = headers.get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
        engineMemberManagement.active(sessionId, destination);

    }

    @EventListener
    public void unSubscribe(SessionUnsubscribeEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        String sessionId = headers.get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
        engineMemberManagement.unBind(sessionId);
    }

    @EventListener
    public void disConnect(SessionDisconnectEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        String sessionId = headers.get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
        engineMemberManagement.unBind(sessionId);

    }


}
