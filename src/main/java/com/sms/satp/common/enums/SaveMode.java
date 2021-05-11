package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum SaveMode implements EnumCommon {
    COVER(1),
    REMAIN(2),
    INCREMENT(3);

    private static final Map<Integer, SaveMode> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(SaveMode::getCode, Function.identity()));


    private final int code;

    SaveMode(int code) {

        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Nullable
    public static SaveMode getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
