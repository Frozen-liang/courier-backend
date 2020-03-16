package com.sms.satp.parser.common;

import com.sms.satp.entity.dto.SelectDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final List<SelectDto> SELECT_DTO_LIST = new ArrayList<>();

    static {
        for (SchemaType schemaType : values()) {
            mappings.put(schemaType.type, schemaType);
            SELECT_DTO_LIST.add(SelectDto.builder()
                .id(schemaType.name())
                .name(schemaType.type).build());
        }
    }


    @Nullable
    public static SchemaType resolve(@Nullable String type) {
        return (StringUtils.isNoneBlank(type) ? mappings.get(type) : null);
    }

    public static List<SelectDto> getSelectDtoList() {
        return SELECT_DTO_LIST;
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
