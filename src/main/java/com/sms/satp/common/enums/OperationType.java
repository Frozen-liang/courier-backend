package com.sms.satp.common.enums;

public enum OperationType {

    ADD(0),
    EDIT(1),
    DELETE(2);

    private final Integer type;

    OperationType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
}
