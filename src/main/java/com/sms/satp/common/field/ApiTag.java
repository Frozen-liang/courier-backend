package com.sms.satp.common.field;

public enum ApiTag implements Field {

    TAG_NAME("tagName"),
    GROUP_NAME("name");

    private final String name;

    ApiTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
