package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum MatchType implements EnumCommon {

    NO_VERIFY(0), VALUE_EQ(1), VALUE_NE(2),
    VALUE_GT(3), VALUE_GE(4), VALUE_LT(5),
    VALUE_LE(6), VALUE_INCLUDE(7), LENGTH_EQ(8),
    LENGTH_NE(9), LENGTH_GT(10), LENGTH_LT(11),
    REGEX_MATCH(12), VALUE_NOT_INCLUDE(13);

    private static final Map<Integer, MatchType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(MatchType::getCode, Function.identity()));

    @NonNull
    public static MatchType getMatchType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }

    private final int code;

    MatchType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
