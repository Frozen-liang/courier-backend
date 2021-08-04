package com.sms.courier.common.listener;

import static com.sms.courier.common.enums.ApiBindingStatus.EXPIRED;

import com.sms.courier.common.listener.event.ApiDeleteEvent;
import com.sms.courier.service.ApiTestCaseService;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
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
        if (CollectionUtils.isEmpty(apiIds)) {
            return;
        }
        apiTestCaseService.updateApiTestCaseStatusByApiId(apiIds, EXPIRED);
    }

}
