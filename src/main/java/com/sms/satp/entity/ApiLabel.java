package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "ApiLabel")
public class ApiLabel {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;
    private String projectId;
    private String labelName;
    /*1:apiLabel 2:apiCaseLabel 3:apiCasePipelineLabel*/
    private Short labelType;
}
