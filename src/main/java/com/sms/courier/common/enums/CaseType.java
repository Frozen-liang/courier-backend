package com.sms.courier.common.enums;

public enum CaseType {

    CASE("caseCount"),
    SCENE_CASE("sceneCaseCount");

    CaseType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }

}
