package com.sms.courier.common.field;

public enum ScheduleField implements Field {

    SCHEDULE_STATUS("scheduleStatus");

    private final String name;

    ScheduleField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
