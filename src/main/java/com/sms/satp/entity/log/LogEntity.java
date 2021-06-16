package com.sms.satp.entity.log;

import com.sms.satp.common.enums.OperationModule;
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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "Log")
public class LogEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private OperationType operationType;

    private OperationModule operationModule;

    private String operationDesc;

    private String operator;

    @CreatedBy
    private Long operatorId;

    @CreatedDate
    private LocalDateTime createDateTime;
}