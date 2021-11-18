package com.sms.courier.common.field;

public enum ScheduleSceneCaseJobField implements Field {

    SCHEDULE_RECORD_ID("scheduleRecordId"),
    SCENE_CASE_ID("SceneCaseId");

    private final String name;

    ScheduleSceneCaseJobField(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
