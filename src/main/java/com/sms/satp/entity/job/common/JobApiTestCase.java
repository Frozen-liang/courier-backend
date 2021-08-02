package com.sms.satp.entity.job.common;

import com.sms.satp.dto.response.ApiEntityResponse;
import com.sms.satp.entity.api.common.AdvancedSetting;
import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseHeadersVerification;
import com.sms.satp.entity.api.common.ResponseResultVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
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
public class JobApiTestCase {

    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    private String caseName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String dataCollId;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private Integer status;

    private Integer responseParamsExtractionType;

    private HttpStatusVerification httpStatusVerification;

    private ResponseHeadersVerification responseHeadersVerification;

    private ResponseResultVerification responseResultVerification;

    private ResponseTimeVerification responseTimeVerification;

    @Field(name = "isExecute")
    private boolean execute;

    private AdvancedSetting advancedSetting;

    private ApiEntityResponse apiEntity;

    private CaseReport caseReport;

}
