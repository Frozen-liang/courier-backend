package com.sms.satp.common.field;

public enum  CommonFiled {

    ID("_id"),
    PROJECT_ID("projectId"),
    CREATE_DATE_TIME("createDateTime"),
    MODIFY_DATE_TIME("modifyDateTime"),
    REMOVE("removed");

    private final String filed;

    CommonFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }
}
