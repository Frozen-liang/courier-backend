package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExecuteType implements EnumCommon {

    PARALLEL(0),
    SERIAL(1);

    private final Integer code;

    @Override
    public int getCode() {
        return this.code;
    }

    ExecuteType(Integer code) {
        this.code = code;
    }

    private static final Map<Integer, ExecuteType> mappings = new HashMap<>(16);

    static {
        for (ExecuteType executeType : values()) {
            mappings.put(executeType.getCode(), executeType);
        }
    }

    public static ExecuteType getExecuteType(Integer code) {
        return mappings.get(code);
    }

}
