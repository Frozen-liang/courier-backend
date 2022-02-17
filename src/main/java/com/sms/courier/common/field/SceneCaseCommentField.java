package com.sms.courier.common.field;

public enum SceneCaseCommentField implements Field {

    SCENE_CASE_ID("sceneCaseId"),
    PARENT_ID("parentId");

    private final String name;

    SceneCaseCommentField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
