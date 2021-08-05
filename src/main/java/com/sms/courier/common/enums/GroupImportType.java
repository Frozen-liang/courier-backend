package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum GroupImportType implements EnumCommon {

    ONLY_ONCE(1),

    EVERY_ONCE(2);

    private static final Map<Integer, GroupImportType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(GroupImportType::getCode, Function.identity()));
    private final int code;

    GroupImportType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static GroupImportType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}
