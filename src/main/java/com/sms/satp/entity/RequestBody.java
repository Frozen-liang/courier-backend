package com.sms.satp.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class RequestBody {

    private String id;
    @Field("ref_schema_id")
    private String refSchemaId;
    private String description;
    @Field("media_types")
    private List<String> mediaTypes;
    private Schema schema;
    private Boolean required;
}
