package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum TaskStatus implements EnumCommon {
    RUNNING(0),
    COMPLETE(1);
    private final int code;
    private static final Map<Integer, TaskStatus> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(TaskStatus::getCode, Function.identity()));

    TaskStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static TaskStatus getType(@Nullable Integer code) {
        return Objects.nonNull(code) ? MAPPINGS.get(code) : null;
    }
}
