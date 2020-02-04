package com.sms.satp.parser.common;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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

    static {
        for (SchemaType schemaType : values()) {
            mappings.put(schemaType.type, schemaType);
        }
    }


    @Nullable
    public static SchemaType resolve(@Nullable String type) {
        return (StringUtils.isNoneBlank(type) ? mappings.get(type) : null);
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
