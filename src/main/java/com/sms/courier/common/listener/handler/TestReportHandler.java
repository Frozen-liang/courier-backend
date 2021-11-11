package com.sms.courier.common.listener.handler;

import com.sms.courier.common.listener.event.TestReportEvent;

public interface TestReportHandler {

    void handle(TestReportEvent event);
}
