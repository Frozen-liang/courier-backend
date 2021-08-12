package com.sms.courier.entity.job;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.common.CaseReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiTestCaseJobReport {

    private String jobId;
    private JobStatus jobStatus;
    private String errCode;
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
    private CaseReport caseReport;
    private String message;
}
