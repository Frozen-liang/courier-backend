package com.sms.courier.common.field;

public enum ScheduleRecordField implements Field {

    JOB_IDS("jobIds"),
    JOB_RECORDS("jobRecords"),
    TEST_COMPLETION_TIME("testCompletionTime"),
    EXECUTE("isExecute"),
    SCHEDULE_NAME("scheduleName"),
    SCHEDULE_ID("scheduleId"),
    SUCCESS("success"),
    FAIL("fail"),
    VERSION("version"),
    EXECUTE_RECORD("executeRecord"),
    JOB_ID("jobId");

    private final String name;

    ScheduleRecordField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
