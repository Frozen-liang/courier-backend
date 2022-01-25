package com.sms.courier.generator.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.HashMap;
import java.util.Map;

public enum CodeType implements EnumCommon {

    CSHARP(0);

    private final Integer code;

    CodeType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    private static final Map<Integer, CodeType> mappings = new HashMap<>(16);

    static {
        for (CodeType codeType : values()) {
            mappings.put(codeType.getCode(), codeType);
        }
    }

    public static CodeType getCodeType(Integer code) {
        return mappings.get(code);
    }
}
