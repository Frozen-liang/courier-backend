package com.sms.courier.common.listener;

import com.sms.courier.common.listener.event.TestReportEvent;
import com.sms.courier.common.listener.handler.TestReportHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestReportListener {

    private final TestReportHandlerFactory testReportHandlerFactory;

    public TestReportListener(TestReportHandlerFactory testReportHandlerFactory) {
        this.testReportHandlerFactory = testReportHandlerFactory;
    }

    @EventListener
    public void doProcess(TestReportEvent event) {
        testReportHandlerFactory.getHandler(event.getType()).handle(event);
    }


}
