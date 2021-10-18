package com.sms.courier.dto.response;

import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestCaseJobPageResponse {

    private Integer jobStatus;
    private String message;
    private String testDateTime;
    private String testUser;
    private CaseReportResponse testReport;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerificationResponse responseHeadersVerification;
    private ResponseResultVerificationResponse responseResultVerification;
    private ResponseTimeVerification responseTimeVerification;
}
