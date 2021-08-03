package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum RoleType implements EnumCommon {

    USER(0),

    ENGINE(1);

    private static final Map<Integer, RoleType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(RoleType::getCode, Function.identity()));
    private final int code;

    RoleType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static RoleType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
