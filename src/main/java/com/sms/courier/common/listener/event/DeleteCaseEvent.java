package com.sms.courier.common.listener.event;

import com.sms.courier.common.enums.CaseType;
import java.util.List;
import lombok.Getter;

@Getter
public class DeleteCaseEvent {

    private final List<String> apiIds;
    private final CaseType caseType;
    private final Integer count;

    public DeleteCaseEvent(List<String> apiIds, CaseType caseType, Integer count) {
        this.apiIds = apiIds;
        this.caseType = caseType;
        this.count = count;
    }
}
