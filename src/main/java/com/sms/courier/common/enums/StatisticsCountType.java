package com.sms.courier.common.enums;

public enum StatisticsCountType {

    API("api"),
    API_TEST_CASE("apiTestCase"),
    SCENE_CASE("sceneCase");

    private final String name;

    StatisticsCountType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
