package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiLabelType implements EnumCommon {
    API(1),
    CASE(2),
    SCENE(3);
    private int code;
    private static final Map<Integer, ApiLabelType> mappings = new HashMap<>(16);

    ApiLabelType(int code) {
        this.code = code;
    }

    static {
        for (ApiLabelType apiLabelType : values()) {
            mappings.put(apiLabelType.getCode(), apiLabelType);
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    public static ApiLabelType getType(int code) {
        return mappings.get(code);
    }
}
