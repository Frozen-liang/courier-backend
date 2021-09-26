package com.sms.courier.common.field;

public enum ScheduleRecordField implements Field {

    JOB_IDS("jobIds"),
    JOB_RECORDS("jobRecords"),
    TEST_COMPLETION_TIME("testCompletionTime"),
    EXECUTE("isExecute"),
    SCHEDULE_NAME("scheduleName"),
    SUCCESS("success"),
    FAIL("fail"),
    VERSION("version");

    private final String name;

    ScheduleRecordField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
