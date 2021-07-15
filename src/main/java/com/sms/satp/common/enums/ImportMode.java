package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum ImportMode {
    COVER(1),
    INCREMENT(2);

    private static final Map<Integer, ImportMode> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(ImportMode::getCode, Function.identity()));

    private final int code;

    ImportMode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public static ImportMode getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
