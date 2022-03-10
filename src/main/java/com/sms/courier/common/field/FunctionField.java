package com.sms.courier.common.field;

public enum FunctionField implements Field {

    FUNCTION_KEY("functionKey");

    private final String name;

    FunctionField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
