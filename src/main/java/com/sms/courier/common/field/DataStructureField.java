package com.sms.courier.common.field;

public enum DataStructureField implements Field {

    NAME("name"),
    DESCRIPTION("description"),
    REF_ID("refId"),
    REF_STRUCT_IDS("refStructIds"),
    STRUCT("struct"),
    STRUCT_TYPE("structType");

    private final String name;

    DataStructureField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
