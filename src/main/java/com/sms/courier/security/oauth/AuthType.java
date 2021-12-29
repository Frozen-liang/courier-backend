package com.sms.courier.security.oauth;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public enum AuthType implements EnumCommon {
    NERKO(0);
    private final int code;
    private static final Map<Integer, AuthType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(AuthType::getCode, Function.identity()));

    AuthType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static AuthType getType(@Nullable Integer code) {
        return MAPPINGS.get(code);
    }
}
