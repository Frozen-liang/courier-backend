package com.sms.satp.common.field;

public enum SceneFiled implements Filed {

    NAME("name"),
    GROUP_ID("groupId"),
    TEST_STATUS("testStatus"),
    TAG_ID("tagId"),
    PRIORITY("priority"),
    CREATE_USER_NAME("createUserName"),
    CASE_TEMPLATE_ID("caseTemplateId"),
    SCENE_CASE_ID("sceneCaseId"),
    STATUS("status"),
    PARENT_ID("parentId"),
    ORDER("order"),
    API_ID("apiTestCase.apiId");

    private final String filedName;

    SceneFiled(String filedName) {
        this.filedName = filedName;
    }

    @Override
    public String getFiled() {
        return this.filedName;
    }
}
