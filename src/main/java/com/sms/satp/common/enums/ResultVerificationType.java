package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum ResultVerificationType implements EnumCommon {

    JSON(0),
    JSON_PATH(1);

    private Integer code;

    ResultVerificationType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    private static final Map<Integer, ResultVerificationType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(ResultVerificationType::getCode, Function.identity()));

    @NonNull
    public static ResultVerificationType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }

}
