package com.sms.courier.entity.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
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
public class SceneCaseJobEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String engineId;
    @Builder.Default
    @JsonIgnore
    private Boolean removed = false;
    private String createUserId;
    private String workspaceId;
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
     * 出错时，是否执行下一个步骤.
     */
    @Field("isNext")
    private boolean next;

    private List<JobSceneCaseApi> apiTestCase;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    /**
     * 测试人员.
     */
    private String createUserName;

    private JobStatus jobStatus;

    private String message;

    private Integer totalTimeCost;

    private Integer paramsTotalTimeCost;

    private Integer delayTimeTotalTimeCost;

    private List<Object> infoList;
}
