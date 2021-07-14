package com.sms.satp.service;

import static com.sms.satp.utils.UserDestinationUtil.getProjectDest;

import com.sms.satp.websocket.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void projectMessage(String projectId, Payload<?> payload) {
        log.info("Send project message. projectId:{},payload{}", projectId, payload);
        simpMessagingTemplate.convertAndSend(getProjectDest(projectId), payload);
    }

    @Override
    public void userMessage(String userId, Payload<?> payload) {
        log.info("Send user message. userId:{},payload{}", userId, payload);
        simpMessagingTemplate.convertAndSend(getProjectDest(userId), payload);
    }
}
