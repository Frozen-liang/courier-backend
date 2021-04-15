package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum RequestMethod implements EnumCommon {


    GET(0), POST(1), PUT(2), PATCH(3), DELETE(4), HEAD(5), OPTIONS(6), TRACE(7);


    private static final Map<Integer, RequestMethod> mappings = new HashMap<>(16);
    private final int code;

    static {
        for (RequestMethod requestMethod : values()) {
            mappings.put(requestMethod.getCode(), requestMethod);
        }
    }

    RequestMethod(int code) {
        this.code = code;
    }

    @Nullable
    public static RequestMethod getType(@Nullable Integer code) {
        return (code != null ? mappings.get(code) : null);
    }

    public static  RequestMethod resolve(String method){
        return null;
    }

    @Override
    public int getCode() {
        return this.code;
    }


}
