package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum OperationType implements EnumCommon {

    ADD(0),
    EDIT(1),
    DELETE(2);

    private static final Map<Integer, OperationType> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(OperationType::getCode, Function.identity()));
    private final int code;

    OperationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static OperationType getType(@Nullable Integer code) {
        return Objects.isNull(code) ? null : MAPPINGS.get(code);
    }
}
