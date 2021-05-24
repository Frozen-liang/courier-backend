package com.sms.satp.common.field;

public enum LogFiled implements Filed {

    OPERATION_TYPE("operationType"),
    OPERATION_MODULE("operationModule"),
    OPERATION_DESC("operationDesc"),
    OPERATOR("operator"),
    OPERATOR_ID("operatorId");

    private final String filed;

    LogFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }
}
