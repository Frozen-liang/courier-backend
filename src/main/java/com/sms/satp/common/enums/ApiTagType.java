package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiTagType implements EnumCommon {
    API(1),
    CASE(2),
    SCENE(3);
    private final int code;
    private static final Map<Integer, ApiTagType> mappings = new HashMap<>(16);

    ApiTagType(int code) {
        this.code = code;
    }

    static {
        for (ApiTagType apiTagType : values()) {
            mappings.put(apiTagType.getCode(), apiTagType);
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    public static ApiTagType getType(int code) {
        return mappings.get(code);
    }
}
