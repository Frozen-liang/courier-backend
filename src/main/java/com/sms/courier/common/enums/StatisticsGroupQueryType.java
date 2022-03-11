package com.sms.courier.common.enums;

public enum StatisticsGroupQueryType {

    API_TEST_CASE("apiTestCase"),
    SCENE_CASE("sceneCase"),
    API_TEST_CASE_JOB("apiTestCaseJob"),
    SCENE_CASE_JOB("sceneCaseJob");

    private final String name;

    StatisticsGroupQueryType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
