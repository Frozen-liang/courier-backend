package com.sms.satp.common.listener.event;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;

@Getter
public class ApiDeleteEvent implements Serializable {

    private static final long serialVersionUID = 7099057708183579526L;

    private final List<String> apiIds;

    public ApiDeleteEvent(List<String> apiIds) {
        this.apiIds = apiIds;
    }
}
