package com.sms.courier.entity.job;

import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobReport;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SceneCaseJobReport extends JobReport {

    private List<CaseReport> caseReportList;

}
