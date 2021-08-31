package com.sms.courier.common.listener.event;

import com.sms.courier.common.enums.CaseType;
import java.util.List;
import lombok.Getter;

@Getter
public class DeleteCaseEvent {

    private final List<String> apiIds;
    private final CaseType caseType;

    public DeleteCaseEvent(List<String> apiIds, CaseType caseType) {
        this.apiIds = apiIds;
        this.caseType = caseType;
    }
}
