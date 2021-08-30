package com.sms.courier.common.field;

public enum ApiTestCaseJobField implements Field {

    API_TEST_CASE_ID("apiTestCase.jobApiTestCase.id"),
    ENGINE_ID("engineId"),
    JOB_STATUS("jobStatus"),
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