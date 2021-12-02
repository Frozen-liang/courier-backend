package com.sms.courier.common.field;

public enum ApiTestCaseJobField implements Field {

    API_TEST_CASE_ID("apiTestCase.jobApiTestCase.id"),
    ENGINE_ID("engineId"),
    JOB_STATUS("jobStatus"),
    TOTAL_TIME_COST("totalTimeCost"),
    PARAMS_TOTAL_TIME_COST("paramsTotalTimeCost"),
    DELAY_TIME_TOTAL_TIME_COST("delayTimeTotalTimeCost"),
    INFO_LIST("infoList"),
    JOB_ENV_ID("environment.id"),
    JOB_API_ID("apiTestCase.jobApiTestCase.jobApi.id");


    ApiTestCaseJobField(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String getName() {
        return name;
    }
}
