package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public enum SchemaType {

    STRING("string", ParamType.STRING),
    INT("integer", ParamType.NUMBER),
    LONG("long", ParamType.NUMBER),
    OBJECT("object", ParamType.OBJECT),
    DOUBLE("double", ParamType.NUMBER),
    BOOLEAN("boolean", ParamType.BOOLEAN),
    DATE("date", ParamType.DATE),
    DATETIME("datetime", ParamType.DATETIME),
    FLOAT("float", ParamType.NUMBER),
    JSON("json", ParamType.JSON),
    NUMBER("number", ParamType.NUMBER),
    FILE("file", ParamType.FILE),
    ARRAY("array", ParamType.ARRAY);


    private final String type;
    private final ParamType paramType;
    private static final Map<String, String> SPECIAL_TYPE = Map
        .of("date-time", DATETIME.getType(), "binary", FILE.getType());
    private static final Map<String, SchemaType> SCHEMA_TYPE_MAP
        = Arrays.stream(values()).collect(Collectors.toMap(SchemaType::getType, Function.identity()));


    @Nullable
    public static SchemaType resolve(@Nullable String type) {
        return SCHEMA_TYPE_MAP.get(type);
    }

    public static SchemaType resolve(@Nullable String type, String format) {

        return resolve(SPECIAL_TYPE.getOrDefault(StringUtils.defaultIfBlank(format, ""), type));
    }

    SchemaType(String type, ParamType paramType) {
        this.type = type;
        this.paramType = paramType;
    }

    public boolean matches(String type) {
        return (this == resolve(type));
    }


    public String getType() {
        return type;
    }

    public ParamType getParamType() {
        return paramType;
    }
}
