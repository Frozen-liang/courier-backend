package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiRequestParamType implements EnumCommon {
    FORM_DATA(0),
    JSON(1),
    XML(2),
    RAW(3),
    BINARY(4);

    private final int code;
    private static final Map<Integer, ApiRequestParamType> mappings = new HashMap<>(16);

    static {
        for (ApiRequestParamType apiRequestParamType : values()) {
            mappings.put(apiRequestParamType.getCode(), apiRequestParamType);
        }
    }
    ApiRequestParamType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ApiRequestParamType getType(int code){
        return mappings.get(code);
    }
}
