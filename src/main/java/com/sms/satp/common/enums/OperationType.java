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
    // 逻辑删除
    DELETE(2),
    SYNC(3),
    // 真实删除
    REMOVE(4),
    CLEAR_RECYCLE_BIN(5),
    RECOVER(6),
    LOCK(7);

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
