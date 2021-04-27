package com.sms.satp.entity.scenetest;

import com.sms.satp.common.enums.OperationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SceneCaseApiLog")
public class SceneCaseApiLog {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseApiId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String templateCaseApiId;

    private OperationType operationType;

    private String operationTarget;

    private String operationUserName;

    @CreatedBy
    private Long createUserId;

    @CreatedDate
    private LocalDateTime createDateTime;
}
