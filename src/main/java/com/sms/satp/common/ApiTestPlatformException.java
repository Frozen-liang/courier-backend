package com.sms.satp.common;

public class ApiTestPlatformException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public ApiTestPlatformException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiTestPlatformException(ErrorCode errorCode, Throwable t) {
        super(errorCode.getMessage(), t);
        this.code = errorCode.getCode();
    }

    public ApiTestPlatformException(ErrorCode errorCode, Throwable t, Object... params) {
        super(String.format(errorCode.getMessage(), params), t);
        this.code = errorCode.getCode();
    }

    public ApiTestPlatformException(ErrorCode errorCode, Object... params) {
        super(String.format(errorCode.getMessage(), params));
        this.code = errorCode.getCode();
    }

    public ApiTestPlatformException(String message, Throwable t) {
        super(message, t);
    }

    public ApiTestPlatformException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
