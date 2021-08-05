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

    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;

    private ApiType apiType;

    private String shell;

    private Integer order;

    private JobApiTestCase jobApiTestCase;
}
