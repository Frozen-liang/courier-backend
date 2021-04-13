package com.sms.satp.entity.function;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "ProjectFunction")
public class ProjectFunction {

    @Id
    private ObjectId id;
    private String projectId;
    private String functionDesc;
    private String functionName;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
    private List<FunctionParam> functionParams;
    private String functionCode;
}
