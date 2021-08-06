package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiStatus implements EnumCommon {

    DESIGN(0),
    PENDING(1),
    DEVELOP(2),
    DOCKING(3),
    TESTING(4),
    PUBLISH(5),
    DEPRECATED(6);

    private final int code;
    private static final Map<Integer, ApiStatus> mappings = new HashMap<>(16);

    static {
        for (ApiStatus apiStatus : values()) {
            mappings.put(apiStatus.getCode(), apiStatus);
        }
    }

    ApiStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static ApiStatus getType(Integer code) {
        return mappings.get(code);
    }
}
