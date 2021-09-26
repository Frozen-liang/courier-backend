package com.sms.courier.entity.schedule;

import com.sms.courier.common.enums.CaseType;
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
public class ScheduleRecordEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String scheduleId;

    private String scheduleName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String workspaceId;

    private CaseType caseType;

    @Field("isExecute")
    @Default
    private boolean execute = true;

    private String message;

    private String code;

    private List<JobRecord> jobRecords;

    private LocalDateTime createDateTime;

    private LocalDateTime testCompletionTime;

    private int success;

    private int fail;

    // 记录此次定时任务所有正在运行job,当jobIds为空,说明此次定时任务都已完成.
    private List<String> jobIds;

    private int version;
}
