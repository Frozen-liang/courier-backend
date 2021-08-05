package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum RawType implements EnumCommon {
    JSON(0),
    XML(1),
    HTML(2),
    TEXT(3);

    private final int code;
    private static final Map<Integer, RawType> mappings = new HashMap<>(16);

    static {
        for (RawType apiRequestParamType : values()) {
            mappings.put(apiRequestParamType.getCode(), apiRequestParamType);
        }
    }

    RawType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static RawType getType(Integer code) {
        return mappings.get(code);
    }
}
