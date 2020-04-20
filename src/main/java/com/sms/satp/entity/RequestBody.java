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
@NoArgsConstructor
@AllArgsConstructor
public class RequestBody {

    @JsonIgnore
    private String id;
    @Field("ref_schema_id")
    private String refSchemaId;
    private String description;
    @Field("media_types")
    private List<String> mediaTypes;
    private Schema schema;
    private Boolean required;
}
