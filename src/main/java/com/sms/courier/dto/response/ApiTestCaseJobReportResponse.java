package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiTestCaseJobReportResponse {

    private String jobId;
    private Integer jobStatus;
    private Integer errCode;
    private CaseReportResponse caseReport;
    private String message;
    private Integer totalTimeCost;
}
