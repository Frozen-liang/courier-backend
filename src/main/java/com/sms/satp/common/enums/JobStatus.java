package com.sms.satp.common.enums;

public enum JobStatus implements EnumCommon {
    SUCCESS(0),
    FAIL(1);
    int code;

    JobStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
