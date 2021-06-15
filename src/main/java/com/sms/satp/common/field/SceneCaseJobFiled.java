package com.sms.satp.common.field;

public enum SceneCaseJobFiled {

    API_TEST_CASE("apiTestCase"),
    JOB_STATUS("jobStatus"),
    CREATE_USER_NAME("createUserName"),
    MESSAGE("message");

    private final String filed;

    SceneCaseJobFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return this.filed;
    }
}
