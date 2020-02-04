package com.sms.satp.common;

public class ApiTestPlatformException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    public ApiTestPlatformException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
