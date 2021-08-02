package com.sms.satp.common.field;

public enum WorkspaceField implements Field {

    USER_IDS("userIds");


    private final String name;

    WorkspaceField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
