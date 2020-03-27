package com.sms.satp.parser.schema;

import com.sms.satp.parser.common.SchemaType;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiSchema {

    private ApiSchema item;
    private String name;
    private String title;
    private List<String> required;
    private SchemaType type;
    private Map<String, ApiSchema> properties;
    private String description;
    private String ref;
    private Boolean deprecated;

}
