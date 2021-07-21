package com.sms.satp.dto.response;

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
}
