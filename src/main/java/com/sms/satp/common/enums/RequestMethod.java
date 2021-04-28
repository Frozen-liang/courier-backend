package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum RequestMethod implements EnumCommon {


    GET(0), POST(1), PUT(2), PATCH(3), DELETE(4), HEAD(5), OPTIONS(6), TRACE(7);

    private static final Map<Integer, RequestMethod> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(RequestMethod::getCode, Function.identity()));

    private static final Map<String, RequestMethod> MAPPINGS_BY_NAME = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(RequestMethod::name, Function.identity()));

    private final int code;


    RequestMethod(int code) {
        this.code = code;
    }

    @Nullable
    public static RequestMethod getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

    public static RequestMethod resolve(String method) {
        return MAPPINGS_BY_NAME.get(method);
    }

    @Override
    public int getCode() {
        return this.code;
    }


}
