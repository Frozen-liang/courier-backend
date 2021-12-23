package com.sms.courier.common.field;

public enum EngineMemberField implements Field {

    OPEN("isOpen"),
    DESTINATION("destination"),
    NAME("name"),
    HOST("host"),
    TASK_COUNT("taskCount"),
    CASE_TASK("caseTask"),
    SCENE_CASE_TASK("sceneCaseTask"),
    TASK_SIZE_LIMIT("taskSizeLimit"),
    STATUS("status");

    private final String name;

    EngineMemberField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
