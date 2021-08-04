package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum NoticeType implements EnumCommon {
    CLOSE(0),
    SUCCESS(1),
    FAIL(2),
    ALL(3);
    private final int code;
    private static final Map<Integer, NoticeType> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(NoticeType::getCode, Function.identity()));

    NoticeType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static NoticeType getType(@Nullable Integer code) {
        return Objects.nonNull(code) ? MAPPINGS.get(code) : null;
    }
}
