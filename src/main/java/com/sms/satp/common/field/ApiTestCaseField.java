package com.sms.satp.common.field;

public enum ApiTestCaseField implements Field {

    TAG_IDS("tagIds");

    private final String name;

    ApiTestCaseField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
