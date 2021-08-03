package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParamType implements EnumCommon {
    STRING(0), FILE(1),

    JSON(2), INT(3),

    FLOAT(4), DOUBLE(5),

    DATE(6), DATETIME(7),

    BOOLEAN(8), BYTE(9), SHORT(10),

    LONG(11), ARRAY(12),

    OBJECT(13), NUMBER(14);

    private static final Map<Integer, ParamType> mappings = new HashMap<>(16);

    static {
        for (ParamType paramType : values()) {
            mappings.put(paramType.getCode(), paramType);
        }
    }

    private final int code;

    ParamType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ParamType getType(int code) {
        return mappings.get(code);
    }
}
