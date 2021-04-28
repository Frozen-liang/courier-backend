package com.sms.satp.common.enums;

import java.util.HashMap;
import java.util.Map;
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


    private String type;
    private ParamType paramType;
    private static final Map<String, SchemaType> mappings = new HashMap<>(16);
    private static final Map<String, String> SPECIAL_TYPE = new HashMap<>();

    static {

        SPECIAL_TYPE.put("date-time", DATETIME.getType());
        SPECIAL_TYPE.put(DATE.getType(), DATE.getType());
        SPECIAL_TYPE.put("binary", FILE.getType());

        for (SchemaType schemaType : values()) {
            mappings.put(schemaType.type, schemaType);
        }
    }


    @Nullable
    public static SchemaType resolve(@Nullable String type) {
        return mappings.get(type);
    }

    public static SchemaType resolve(@Nullable String type, String format) {
        return resolve(SPECIAL_TYPE.getOrDefault(format, type));
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
