package com.sms.satp.controller;

import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import com.sms.satp.websocket.Greeting;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChannelController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private final EngineMemberManagement engineMemberManagement;

    public ChannelController(EngineMemberManagement engineMemberManagement) {
        this.engineMemberManagement = engineMemberManagement;
    }

    @MessageMapping("v1/report")
    public void report(@Payload Greeting message) throws Exception {
        Thread.sleep(1000); // simulated delay
        log.info("Receive message {}", message);
        Set<String> availableIds = engineMemberManagement.getAvailableMembers();
        availableIds.stream().findFirst()
            .ifPresent(destination -> simpMessagingTemplate.convertAndSend(destination, message));

    }

    @PostMapping("v1/engine/bind")
    public String bind(@Validated @RequestBody EngineRegistrationRequest request) {
        return engineMemberManagement.bind(request);
    }
}
