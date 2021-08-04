package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiTagType implements EnumCommon {
    API(0),
    CASE(1),
    SCENE(2);
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

    public static ApiTagType getType(Integer code) {
        return mappings.get(code);
    }
}
