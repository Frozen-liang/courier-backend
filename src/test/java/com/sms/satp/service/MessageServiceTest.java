package com.sms.satp.service;

import com.sms.satp.service.impl.MessageServiceImpl;
import com.sms.satp.websocket.Payload;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

/**
 * @author zixi.gao
 * @create 2021-07-16 16:30
 */
@DisplayName("Tests for MessageService")
public class MessageServiceTest {
    private final SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
    private final MessageService messageService = new MessageServiceImpl(simpMessagingTemplate);
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the projectMessage method in the Message Service")
    public void projectMessage_test() {
        messageService.projectMessage(ID, Payload.ok(null));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));

    }

    @Test
    @DisplayName("Test the userMessage method in the Message Service")
    public void userMessage_test() {
        messageService.userMessage(ID, Payload.ok(null));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }
}
