package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum MockApiResponseParamType implements EnumCommon {

    FOLLOW_API_DOC(0),
    JSON(1),
    XML(2),
    RAW(3);

    private static final Map<Integer, MockApiResponseParamType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(MockApiResponseParamType::getCode, Function.identity()));
    private final int code;

    MockApiResponseParamType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static MockApiResponseParamType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
