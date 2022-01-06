package com.sms.courier.security.oauth;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public enum OAuthType implements EnumCommon {
    NERKO(0, "Nerko");
    private final int code;
    private final String name;
    private static final Map<Integer, OAuthType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(OAuthType::getCode, Function.identity()));

    OAuthType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OAuthType getType(@Nullable Integer code) {
        return MAPPINGS.get(code);
    }
}
