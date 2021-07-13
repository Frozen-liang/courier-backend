package com.sms.satp.common.enums;

public enum ResponseParamsExtractionType implements EnumCommon {

    JSON(0),
    RAW(1);

    private Integer code;

    ResponseParamsExtractionType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
