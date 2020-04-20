package com.sms.satp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.satp.parser.common.SchemaType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Schema {

    @Id
    @JsonIgnore
    @Field("_id")
    private String id;
    @Field("project_id")
    private String projectId;
    private String name;
    private String title;
    private Schema item;
    private List<String> required;
    private SchemaType type;
    private Map<String, Schema> properties;
    private String description;
    private Boolean deprecated;
    @JsonIgnore
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @JsonIgnore
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;
}
