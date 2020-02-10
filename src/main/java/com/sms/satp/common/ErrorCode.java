package com.sms.satp.common;

public enum ErrorCode {

    SYSTEM_ERROR("9999", "System error"),
    PARSER_OPEN_API_ERROR("30001", "Document of OpenApi parsing has failed"),
    GET_REF_KEY_ERROR("40001", "The reference value cannot be null"),
    DOCUMENT_TYPE_ERROR("4002", "Document type error"),
    // 60001 describes the test exception
    NOT_SUPPORT_METHOD("60001", "Does not support other HTTP methods.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
