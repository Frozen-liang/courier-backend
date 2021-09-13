package com.sms.courier.common.field;

public enum EngineMemberField implements Field {

    OPEN("isOpen");

    private final String name;

    EngineMemberField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
