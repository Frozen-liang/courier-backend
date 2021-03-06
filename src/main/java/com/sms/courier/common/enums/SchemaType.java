package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public enum SchemaType {

    STRING("string", ParamType.STRING),
    INT("integer", ParamType.INT),
    LONG("long", ParamType.LONG),
    OBJECT("object", ParamType.OBJECT),
    DOUBLE("double", ParamType.DOUBLE),
    BOOLEAN("boolean", ParamType.BOOLEAN),
    DATE("date", ParamType.DATE),
    DATETIME("datetime", ParamType.DATETIME),
    FLOAT("float", ParamType.FLOAT),
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
        if (StringUtils.isBlank(type)) {
            return null;
        }
        return SCHEMA_TYPE_MAP.get(type.toLowerCase(Locale.ROOT));
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
