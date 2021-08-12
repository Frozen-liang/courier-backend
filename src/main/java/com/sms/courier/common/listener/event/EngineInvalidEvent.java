package com.sms.courier.common.listener.event;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;

@Getter
public class EngineInvalidEvent implements Serializable {

    private static final long serialVersionUID = 7099057708183965876L;

    private final List<String> engineIds;

    public EngineInvalidEvent(List<String> engineIds) {
        this.engineIds = engineIds;
    }
}
