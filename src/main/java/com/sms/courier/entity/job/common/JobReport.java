package com.sms.courier.entity.job.common;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.JobType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class JobReport {

    private String jobId;
    private JobStatus jobStatus;
    private JobType jobType;
    private String message;
    private String errCode;
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
    private Integer delayTimeTotalTimeCost;
    private List<Object> infoList;
}
