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
public class SceneCaseJobReportResponse {

    private String jobId;
    private Integer jobStatus;
    private List<CaseReportResponse> caseReportList;
    private String message;
    private Integer totalTimeCost;
}
