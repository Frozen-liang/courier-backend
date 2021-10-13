package com.sms.courier.common.field;

public enum ApiTagField implements Field {

    TAG_NAME("tagName"),
    GROUP_NAME("name");

    private final String name;

    ApiTagField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
