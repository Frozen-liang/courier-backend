package com.sms.satp.service.impl;

import static com.sms.satp.utils.UserDestinationUtil.getProjectDest;

import com.sms.satp.entity.function.FunctionMessage;
import com.sms.satp.service.MessageService;
import com.sms.satp.websocket.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final String FUNCTION_DEST = "/engine/pull/function";

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

    @Override
    public void enginePullFunctionMessage(FunctionMessage functionMessage) {
        log.info("Send engine message.functionIds:{},operationType:{}", functionMessage.getIds(),
            functionMessage.getOperationType());
        simpMessagingTemplate.convertAndSend(FUNCTION_DEST, Payload.ok(functionMessage));
    }
}
