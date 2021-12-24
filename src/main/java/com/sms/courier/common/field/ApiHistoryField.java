package com.sms.courier.common.field;

public enum ApiHistoryField implements Field {

    API_ID("record._id"),
    TAG_ID("record.tagId"),
    API_MANAGER_ID("record.apiManagerId"),
    GROUP_ID("record.groupId"),
    CREATE_USER_ID("record.createUserId"),
    RECORD_("record.");

    private final String name;

    ApiHistoryField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
