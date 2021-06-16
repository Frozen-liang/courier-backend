package com.sms.satp.common.listener;

import static com.sms.satp.common.enums.ApiBindingStatus.EXPIRED;

import com.sms.satp.common.listener.event.ApiDeleteEvent;
import com.sms.satp.service.ApiTestCaseService;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApiDeleteListener {

    private final ApiTestCaseService apiTestCaseService;

    public ApiDeleteListener(ApiTestCaseService apiTestCaseService) {
        this.apiTestCaseService = apiTestCaseService;
    }

    @EventListener
    public void doProcess(ApiDeleteEvent apiDeleteEvent) {
        List<String> apiIds = apiDeleteEvent.getApiIds();
        apiTestCaseService.updateApiTestCaseStatusByApiId(apiIds, EXPIRED);
    }

}
