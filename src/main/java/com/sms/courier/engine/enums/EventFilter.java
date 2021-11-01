package com.sms.courier.engine.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EventFilter {
    START("start"),
    STOP("stop");

    private final String event;
    private final Map<String, EventFilter> mappings =
        Arrays.stream(values()).collect(Collectors.toMap(EventFilter::getEvent, Function.identity()));

    EventFilter(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    private EventFilter resolver(String event) {
        return mappings.get(event);
    }
}
