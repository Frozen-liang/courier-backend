package com.sms.satp.common.field;

public enum ApiFiled implements Filed {

    TAG_ID("tagId"),
    GROUP_ID("groupId"),
    API_PROTOCOL("apiProtocol"),
    REQUEST_METHOD("requestMethod"),
    API_STATUS("apiStatus");

    private final String filed;

    ApiFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }
}
