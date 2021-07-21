package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResultType implements EnumCommon {

    FAIL(0), SUCCESS(1);

    private static final Map<Integer, ResultType> mappings = new HashMap<>(16);

    static {
        for (ResultType apiType : values()) {
            mappings.put(apiType.getCode(), apiType);
        }
    }

    private final Integer code;

    ResultType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ResultType getType(Integer code) {
        return mappings.get(code);
    }
}
