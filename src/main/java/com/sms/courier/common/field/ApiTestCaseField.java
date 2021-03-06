package com.sms.courier.common.field;

public enum ApiTestCaseField implements Field {

    TAG_IDS("tagIds"),
    CASE_API_ID("apiEntity._id"),
    STATUS("status"),
    CASE_NAME("caseName");

    private final String name;

    ApiTestCaseField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
