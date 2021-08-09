package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.entity.api.common.AdvancedSetting;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiTestCaseResponse extends LookupUserResponse {

    private String caseName;
    private String projectId;
    private List<String> tagName;
    private List<String> tagId;
    private Integer responseParamsExtractionType;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerificationResponse responseHeadersVerification;
    private ResponseResultVerificationResponse responseResultVerification;
    private ResponseTimeVerification responseTimeVerification;
    private String dataCollId;
    @Field("isExecute")
    @JsonProperty("isExecute")
    private boolean execute;
    private AdvancedSetting advancedSetting;
    private Integer isSuccess;
    private String testTime;
    private String jobId;
    private ApiResponse apiEntity;
}
