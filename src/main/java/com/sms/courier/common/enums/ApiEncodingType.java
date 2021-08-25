package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum ApiEncodingType implements EnumCommon {

    UTF_8(0),
    GBK(1);

    private static final Map<Integer, ApiEncodingType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(ApiEncodingType::getCode, Function.identity()));
    private final int code;

    ApiEncodingType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static ApiEncodingType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
