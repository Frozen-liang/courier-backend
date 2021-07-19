package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum ResponseParamsExtractionType implements EnumCommon {

    JSON(0),
    RAW(1);

    private final int code;

    ResponseParamsExtractionType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    private static final Map<Integer, ResponseParamsExtractionType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(ResponseParamsExtractionType::getCode, Function.identity()));

    @NonNull
    public static ResponseParamsExtractionType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }
}
