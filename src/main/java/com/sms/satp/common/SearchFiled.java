package com.sms.satp.common;

public enum SearchFiled {

    PROJECT_ID("projectId"),
    NAME("name"),
    GROUP_ID("groupId"),
    TEST_STATUS("testStatus"),
    CASE_TAG("caseTag"),
    PRIORITY("priority"),
    CREATE_USER_NAME("createUserName"),
    STATUS("status");

    private String filedName;

    SearchFiled(String filedName) {
        this.filedName = filedName;
    }

    public String getFiledName() {
        return this.filedName;
    }
}
