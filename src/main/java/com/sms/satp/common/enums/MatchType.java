package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum MatchType implements EnumCommon {

    NO_VERIFY(0), VALUE_EQ(1), VALUE_NE(2),
    VALUE_GT(3), VALUE_GE(4), VALUE_LT(5),
    VALUE_LE(6), VALUE_INCLUDE(7), LENGTH_EQ(8),
    LENGTH_NE(9), LENGTH_GT(10), LENGTH_LT(11),
    REGEX_MATCH(12);

    private static final Map<Integer, MatchType> mappings = new HashMap<>(16);

    static {
        for (MatchType matchType : values()) {
            mappings.put(matchType.getCode(), matchType);
        }
    }

    private Integer code;

    MatchType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static MatchType getMatchType(Integer code) {
        return mappings.get(code);
    }
}
