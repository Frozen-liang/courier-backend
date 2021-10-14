package com.sms.courier.common.field;

public enum ApiCommentField implements Field {

    API_ID("apiId");

    private final String name;

    ApiCommentField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
