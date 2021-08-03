package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum In {
    QUERY,
    HEADER,
    PATH,
    COOKIE;

    private static final Map<String, In> mappings = new HashMap<>(16);

    static {
        for (In in : values()) {
            mappings.put(in.name(), in);
        }
    }


    @Nullable
    public static In resolve(@Nullable String scope) {
        return (scope != null ? mappings.get(scope) : null);
    }


    public boolean matches(String scope) {
        return (this == resolve(scope));
    }
}
