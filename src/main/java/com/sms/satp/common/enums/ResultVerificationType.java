package com.sms.satp.common.enums;

public enum ResultVerificationType implements EnumCommon {

    JSON(0),
    JSON_PATH(1);

    private Integer code;

    ResultVerificationType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
