package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiBindingStatus implements EnumCommon {
    BINDING(0),
    NOT_BINDING(1),
    EXPIRED(2);

    private static final Map<Integer, ApiBindingStatus> mappings = new HashMap<>(16);

    static {
        for (ApiBindingStatus status : values()) {
            mappings.put(status.getCode(), status);
        }
    }

    private Integer code;

    ApiBindingStatus(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ApiBindingStatus getApiBindingStatus(Integer code) {
        return mappings.get(code);
    }
}
