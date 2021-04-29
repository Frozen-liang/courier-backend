package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ApiJsonType implements EnumCommon {
    OBJECT(0, ParamType.OBJECT),
    ARRAY(1, ParamType.ARRAY);

    private final int code;


    private final ParamType paramType;

    private static final Map<Integer, ApiJsonType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(ApiJsonType::getCode, Function.identity()));
    private static final Map<ParamType, ApiJsonType> PARAM_TYPE_API_JSON_TYPE_MAPPINGS = Arrays.stream(values())
        .sequential().collect(
            Collectors.toMap(ApiJsonType::getParamType, Function.identity()));

    ApiJsonType(int code, ParamType paramType) {
        this.code = code;
        this.paramType = paramType;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public static ApiJsonType getType(int code) {
        return MAPPINGS.get(code);
    }

    public static ApiJsonType getTypeByParamType(ParamType paramType) {
        return PARAM_TYPE_API_JSON_TYPE_MAPPINGS.get(paramType);
    }
}
