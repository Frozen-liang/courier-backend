package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DataStructureListResponse extends BaseResponse {

    private String name;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String workspaceId;

    private boolean global;

    private Integer structType;

    private String description;

    @JsonProperty("isQuoted")
    private boolean quoted;

}
