package com.sms.satp.entity.job;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.job.common.CaseReport;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneCaseJobReport {

    private String jobId;
    private JobStatus jobStatus;
    private List<CaseReport> caseReportList;
    private String message;
}
