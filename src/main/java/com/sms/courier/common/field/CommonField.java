package com.sms.courier.common.field;

public enum CommonField implements Field {

    ID("_id"),
    PROJECT_ID("projectId"),
    WORKSPACE_ID("workspaceId"),
    API_ID("apiEntity._id"),
    CREATE_DATE_TIME("createDateTime"),
    MODIFY_DATE_TIME("modifyDateTime"),
    REMOVE("isRemoved"),
    CREATE_USER_ID("createUserId"),
    MODIFY_USER_ID("modifyUserId"),
    GROUP_ID("groupId"),
    USERNAME("username"),
    REF_ID("refId"),
    JOB_STATUS("jobStatus"),
    API_ID_GROUP_SEARCH("apiEntity.id"),
    CREATE_USER_NAME("createUserName"),
    SOURCE_ID("sourceId");

    private final String name;

    CommonField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
