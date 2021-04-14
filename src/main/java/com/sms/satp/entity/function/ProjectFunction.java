package com.sms.satp.entity.function;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "ProjectFunction")
public class ProjectFunction {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;
    private String projectId;
    private String functionDesc;
    private String functionName;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
    private List<FunctionParam> functionParams;
    private String functionCode;
}
