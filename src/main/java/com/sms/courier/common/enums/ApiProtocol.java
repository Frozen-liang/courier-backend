package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApiProtocol implements EnumCommon {
    HTTP(0),
    HTTPS(1),
    WS(2),
    WSS(3);
    private final int code;
    private static final Map<Integer, ApiProtocol> mappings = new HashMap<>(16);

    static {
        for (ApiProtocol apiProtocol : values()) {
            mappings.put(apiProtocol.getCode(), apiProtocol);
        }
    }

    ApiProtocol(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static ApiProtocol getType(Integer code) {
        return mappings.get(code);
    }
}
