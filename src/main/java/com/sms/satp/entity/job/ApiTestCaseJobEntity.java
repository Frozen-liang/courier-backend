package com.sms.satp.entity.job;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ApiTestCaseJob")
public class ApiTestCaseJobEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String engineId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String workspaceId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Builder.Default
    @Field("isRemoved")
    private boolean removed = false;

    private String createUserId;

    private String modifyUserId;

    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime modifyDateTime;

    private JobCaseApi apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private JobStatus jobStatus;

    private String message;
    /**
     * 测试人员.
     */
    private String createUserName;

    private Integer totalTimeCost;
}
