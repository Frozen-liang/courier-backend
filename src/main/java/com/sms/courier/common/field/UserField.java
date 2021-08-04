package com.sms.courier.common.field;

public enum UserField implements Field {

    NICKNAME("nickname"),
    USERNAME("username");

    private final String name;

    UserField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
