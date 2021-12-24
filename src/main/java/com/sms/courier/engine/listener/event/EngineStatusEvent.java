package com.sms.courier.engine.listener.event;

import com.sms.courier.engine.enums.EngineStatus;
import lombok.Getter;

@Getter
public class EngineStatusEvent {

    public EngineStatusEvent(String name, EngineStatus status) {
        this.name = name;
        this.status = status;
    }

    private final String name;
    private final EngineStatus status;


}
