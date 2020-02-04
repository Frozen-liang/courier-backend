package com.sms.satp.parser.model;

import com.sms.satp.parser.schema.ApiSchema;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiDocument {

    private ApiInfo info;
    private List<ApiTag> tags;
    private List<ApiPath> paths;
    private Map<String, ApiSchema> schemas;

}
