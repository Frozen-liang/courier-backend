package com.sms.courier.entity.job;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.common.CaseReport;
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
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
    private Integer delayTimeTotalTimeCost;
    private List<String> infoList;
}
