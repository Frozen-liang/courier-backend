package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.entity.api.common.AdvancedSetting;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApiTestCaseResponse {

    private String id;
    private String caseName;
    private Integer responseParamsExtractionType;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerificationResponse responseHeadersVerification;
    private ResponseResultVerificationResponse responseResultVerification;
    private ResponseTimeVerification responseTimeVerification;
    private String dataCollId;
    private Integer isSuccess;
    private String testTime;
    @JsonProperty("isExecute")
    private boolean execute;
    private AdvancedSetting advancedSetting;
    private JobApiResponse jobApi;
    private CaseReportResponse caseReport;

}
