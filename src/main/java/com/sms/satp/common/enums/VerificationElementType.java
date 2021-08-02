package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum VerificationElementType implements EnumCommon {

    ALL(0), FIRST_ONE(1);

    private static final Map<Integer, VerificationElementType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(VerificationElementType::getCode, Function.identity()));

    @NonNull
    public static VerificationElementType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }

    private final int code;

    VerificationElementType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
