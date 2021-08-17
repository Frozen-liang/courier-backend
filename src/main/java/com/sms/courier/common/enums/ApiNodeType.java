package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiNodeType implements EnumCommon {
    RICH_TEXT(0),
    MARKDOWN(1);

    private final int code;
    private static final Map<Integer, ApiNodeType> mappings = new HashMap<>(16);

    static {
        for (ApiNodeType apiNodeType : values()) {
            mappings.put(apiNodeType.getCode(), apiNodeType);
        }
    }

    ApiNodeType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ApiNodeType getType(Integer code) {
        return mappings.get(code);
    }
}
