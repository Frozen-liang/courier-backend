package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum ImportStatus implements EnumCommon {
    RUNNING(1),
    SUCCESS(2),
    FAILED(3);

    private static final Map<Integer, ImportStatus> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(ImportStatus::getCode, Function.identity()));
    private final int code;

    ImportStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static ImportStatus getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
