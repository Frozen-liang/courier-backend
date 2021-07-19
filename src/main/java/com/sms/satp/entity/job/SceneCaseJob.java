package com.sms.satp.entity.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "SceneCaseJob")
public class SceneCaseJob {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Builder.Default
    @JsonIgnore
    private Boolean removed = false;
    private String createUserId;
    private String modifyUserId;
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    /**
     * 是否锁定，当前步骤出错或未通过时，依然执行下一个步骤.
     */
    @Field("isLock")
    private boolean lock;

    private List<JobSceneCaseApi> apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    /**
     * 测试人员.
     */
    private String createUserName;

    private JobStatus jobStatus;

    private String message;
}
