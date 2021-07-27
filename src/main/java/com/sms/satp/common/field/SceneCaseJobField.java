package com.sms.satp.common.field;

public enum SceneCaseJobField {

    API_TEST_CASE("apiTestCase"),
    JOB_STATUS("jobStatus"),
    CREATE_USER_NAME("createUserName"),
    MESSAGE("message");

    private final String name;

    SceneCaseJobField(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
