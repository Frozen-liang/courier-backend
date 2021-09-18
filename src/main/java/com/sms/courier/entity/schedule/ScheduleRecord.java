package com.sms.courier.entity.schedule;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "ScheduleRecord")
public class ScheduleRecord {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String scheduleId;

    private String scheduleName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field("isExecute")
    @Default
    private boolean execute = true;

    private String message;

    private String code;

    private List<JobRecord> jobRecords;

    private LocalDateTime createDateTime;
}
