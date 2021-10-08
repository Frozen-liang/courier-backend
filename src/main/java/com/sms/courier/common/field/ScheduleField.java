package com.sms.courier.common.field;

public enum ScheduleField implements Field {

    SCHEDULE_STATUS("scheduleStatus"),
    NAME("name"),
    LAST_TASK_COMPLETE_TIME("lastTaskCompleteTime"),
    OPEN("isOpen"),
    TASK_STATUS("taskStatus");

    private final String name;

    ScheduleField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
