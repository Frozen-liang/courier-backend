package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum CaseFilter implements EnumCommon {
    ALL(0),
    PRIORITY_AND_TAG(1),
    CUSTOM(2);
    private final int code;
    private static final Map<Integer, CaseFilter> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(CaseFilter::getCode, Function.identity()));

    CaseFilter(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static CaseFilter getType(@Nullable Integer code) {
        return Objects.nonNull(code) ? MAPPINGS.get(code) : null;
    }
}
