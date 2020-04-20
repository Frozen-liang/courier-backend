package com.sms.satp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    @JsonIgnore
    private String id;
    @Field("ref_schema_id")
    private String refSchemaId;
    @Field("media_types")
    private List<String> mediaTypes;
    private String description;
    private List<Header> headers;
    private Schema schema;
}
