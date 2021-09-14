package com.sms.courier.common.field;

public enum EngineMemberField implements Field {

    OPEN("isOpen"),
    DESTINATION("destination"),
    TASK_COUNT("taskCount"),
    CASE_TASK("caseTask"),
    SCENE_CASE_TASK("sceneCaseTask");

    private final String name;

    EngineMemberField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
