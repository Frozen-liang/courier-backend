package com.sms.satp.entity.test;

import com.sms.satp.parser.common.SchemaType;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseSchema {

    private List<CaseSchema> items;
    private SchemaType type;
    private Object value;
    private Map<String, CaseSchema> properties;
}