package com.sms.satp.common.field;

public enum LogField implements Field {

    OPERATION_TYPE("operationType"),
    OPERATION_MODULE("operationModule"),
    OPERATION_DESC("operationDesc"),
    OPERATOR("operator"),
    OPERATOR_ID("operatorId");

    private final String name;

    LogField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
