package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiType implements EnumCommon {

    API(0), SHELL(1);

    private static final Map<Integer, ApiType> mappings = new HashMap<>(16);

    static {
        for (ApiType apiType : values()) {
            mappings.put(apiType.getCode(), apiType);
        }
    }

    private Integer code;

    ApiType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ApiType getApiType(Integer code) {
        return mappings.get(code);
    }
}
