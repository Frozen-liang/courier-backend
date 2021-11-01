package com.sms.courier.entity.job.common;

import com.sms.courier.common.enums.JobStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class JobEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String engineId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String workspaceId;
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private JobStatus jobStatus;

    private String errCode;

    private String message;

    private Integer totalTimeCost;

    private Integer paramsTotalTimeCost;

    private Integer delayTimeTotalTimeCost;

    private List<Object> infoList;
}
