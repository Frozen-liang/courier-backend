package com.sms.satp.entity.test;

import com.sms.satp.parser.common.SchemaType;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseSchema {

    private String name;
    private SchemaType type;
    private Object value;
    private Map<String, CaseSchema> properties;
}