package com.sms.satp.entity.job;

import com.sms.satp.common.enums.ApiType;
import com.sms.satp.entity.job.common.JobApiTestCase;
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

    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    private Integer apiType;

    private String shell;

    private Integer order;

    private JobApiTestCase jobApiTestCase;
}
