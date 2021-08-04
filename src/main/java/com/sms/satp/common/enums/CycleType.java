package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum CycleType implements EnumCommon {
    DAY(0),
    WEEK(1);
    private final int code;
    private static final Map<Integer, CycleType> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(CycleType::getCode, Function.identity()));

    CycleType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static CycleType getType(@Nullable Integer code) {
        return Objects.nonNull(code) ? MAPPINGS.get(code) : null;
    }
}
