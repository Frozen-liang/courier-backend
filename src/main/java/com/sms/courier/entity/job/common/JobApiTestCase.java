package com.sms.courier.entity.job.common;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ResponseParamsExtractionType;
import com.sms.courier.entity.api.common.AdvancedSetting;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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

    private ApiBindingStatus status;

    private ResponseParamsExtractionType responseParamsExtractionType;

    private HttpStatusVerification httpStatusVerification;

    private ResponseHeadersVerification responseHeadersVerification;

    private ResponseResultVerification responseResultVerification;

    private ResponseTimeVerification responseTimeVerification;

    @Field(name = "isExecute")
    private boolean execute;

    @Default
    private AdvancedSetting advancedSetting = new AdvancedSetting();

    private JobApi jobApi;

    private CaseReport caseReport;

}
