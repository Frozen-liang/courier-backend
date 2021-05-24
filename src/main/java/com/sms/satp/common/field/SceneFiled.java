package com.sms.satp.common.field;

public enum SceneFiled implements Filed{

    NAME("name"),
    GROUP_ID("groupId"),
    TEST_STATUS("testStatus"),
    CASE_TAG("caseTag"),
    PRIORITY("priority"),
    CREATE_USER_NAME("createUserName"),
    STATUS("status"),
    ORDER_NUMBER("orderNumber");

    private final String filedName;

    SceneFiled(String filedName) {
        this.filedName = filedName;
    }

    public String getFiledName() {
        return this.filedName;
    }

    @Override
    public String getFiled() {
        return this.filedName;
    }
}
