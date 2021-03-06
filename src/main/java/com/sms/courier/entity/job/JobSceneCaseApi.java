package com.sms.courier.entity.job;

import com.sms.courier.common.enums.ApiType;
import com.sms.courier.entity.job.common.JobApiTestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSceneCaseApi {

    private String caseId;

    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    private ApiType apiType;

    private String shell;

    @Field(targetType = FieldType.OBJECT_ID)
    private String databaseId;

    private String sql;

    @Field(name = "isSqlResult")
    private boolean sqlResult;

    private JobDatabase jobDatabase;

    private Integer order;

    /**
     * 是否强制执行该步骤，即使其他步骤出错时.
     */
    @Field("isLock")
    private boolean lock;

    private JobApiTestCase jobApiTestCase;
}
