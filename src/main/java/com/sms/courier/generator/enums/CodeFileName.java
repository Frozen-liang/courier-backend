package com.sms.courier.generator.enums;

public enum CodeFileName {

    CONTROLLER("Controller"),
    SERVICE("Service"),
    ENTITY("Entity");

    private final String name;

    CodeFileName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
