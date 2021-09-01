package com.sms.courier.common.field;

public enum SceneCaseJobField {

    API_TEST_CASE("apiTestCase"),
    JOB_STATUS("jobStatus"),
    CREATE_USER_NAME("createUserName"),
    ENGINE_ID("engineId"),
    TOTAL_TIME_COST("totalTimeCost"),
    PARAMS_TOTAL_TIME_COST("paramsTotalTimeCost"),
    DELAY_TIME_TOTAL_TIME_COST("delayTimeTotalTimeCost"),
    INFO_LIST("infoList"),
    MESSAGE("message");

    private final String name;

    SceneCaseJobField(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
