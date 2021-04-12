package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "ApiLabel")
public class ApiLabel {

    @Id
    @Field("_id")
    private ObjectId id;
    @Field("project_id")
    private String projectId;
    @Field("label_name")
    private String labelName;
    /*1:apiLabel 2:apiCaseLabel 3:apiCasePipelineLabel*/
    @Field("label_type")
    private Short labelType;
}
