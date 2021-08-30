package com.sms.courier.dto.response;

import java.util.List;
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
    private String errCode;
    private CaseReportResponse caseReport;
    private String message;
    private Integer paramsTotalTimeCost;
    private Integer totalTimeCost;
    private List<String> infoList;
}
