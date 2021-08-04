package com.sms.courier.service;

import com.sms.courier.entity.function.FunctionMessage;
import com.sms.courier.websocket.Payload;

public interface MessageService {

    void projectMessage(String projectId, Payload<?> payload);

    void userMessage(String userId, Payload<?> payload);

    void enginePullFunctionMessage(FunctionMessage functionMessage);
}
