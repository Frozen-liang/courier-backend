package com.sms.courier.common.field;

public enum ApiField implements Field {

    TAG_ID("tagId"),
    GROUP_ID("groupId"),
    API_PROTOCOL("apiProtocol"),
    API_NAME("apiName"),
    API_PATH("apiPath"),
    REQUEST_METHOD("requestMethod"),
    API_STATUS("apiStatus");

    private final String name;

    ApiField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
