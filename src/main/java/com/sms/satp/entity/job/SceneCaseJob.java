package com.sms.satp.entity.job;

import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "SceneCaseJob")
public class SceneCaseJob extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    /**
     * 是否锁定，当前步骤出错或未通过时，依然执行下一个步骤.
     */
    private Boolean isLock;

    private List<JobSceneCaseApi> caseList;

    private JobEnvironment environment;

    private JobDataCollection dataCollection;

    private Long runTime;

    /**
     * 测试人员.
     */
    private String createUserName;

    private Integer jobStatus;
}
