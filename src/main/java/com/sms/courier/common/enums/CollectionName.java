package com.sms.courier.common.enums;


public enum CollectionName {

    API("Api"),
    API_TAG("ApiTag"),
    USER("User"),
    API_GROUP("ApiGroup"),
    CASE_TEMPLATE_GROUP("CaseTemplateGroup"),
    SCENE_CASE_GROUP("SceneCaseGroup"),
    SCHEDULE("Schedule"),
    USER_GROUP("UserGroup"),
    DATA_STRUCTURE("DataStructure"),
    WORKSPACE("Workspace"),
    API_HISTORY("ApiHistory");

    private final String name;

    CollectionName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
