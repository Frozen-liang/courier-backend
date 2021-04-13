package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "ApiLabel")
public class ApiLabel {

    @Id
    private ObjectId id;
    private String projectId;
    private String labelName;
    /*1:apiLabel 2:apiCaseLabel 3:apiCasePipelineLabel*/
    private Short labelType;
}
