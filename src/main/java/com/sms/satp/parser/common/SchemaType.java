package com.sms.satp.parser.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum SchemaType {

    STRING("string"),
    INT("integer"),
    LONG("long"),
    OBJECT("object"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    DATE("date"),
    DATETIME("datetime"),
    FLOAT("float"),
    JSON("json"),
    NUMBER("number"),
    FILE("file"),
    ARRAY("array");


    private String type;
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

    public boolean matches(String type) {
        return (this == resolve(type));
    }

    SchemaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
