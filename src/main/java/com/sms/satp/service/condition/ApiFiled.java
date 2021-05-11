package com.sms.satp.service.condition;

public enum ApiFiled {

    PROJECT_ID("projectId"),
    TAG_ID("tagId"),
    GROUP_ID("groupId"),
    API_PROTOCOL("apiProtocol"),
    REQUEST_METHOD("requestMethod"),
    API_REQUEST_PARAM_TYPE("apiRequestParamType"),
    API_STATUS("apiStatus");

    private final String filed;

    ApiFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }
}
