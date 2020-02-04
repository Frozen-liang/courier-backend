package com.sms.satp.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class Response {

    private String id;
    @Field("ref_schema_id")
    private String refSchemaId;
    @Field("media_types")
    private List<String> mediaTypes;
    private String description;
    private List<Header> headers;
    private Schema schema;
}
