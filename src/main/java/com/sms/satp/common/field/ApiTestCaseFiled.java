package com.sms.satp.common.field;

public enum ApiTestCaseFiled implements Filed {

    TAG_IDS("tagIds");

    private final String filed;

    ApiTestCaseFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }
}
