package com.sms.courier.dto.response;

import com.sms.courier.common.enums.JobType;
import com.sms.courier.entity.job.common.JobDataCollection;
import com.sms.courier.entity.job.common.JobEnvironment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JobResponse extends BaseResponse {

    private String engineId;
    private String workspaceId;
    private String projectId;
    private JobEnvironment environment;
    private JobDataCollection dataCollection;
    private Integer jobStatus;
    private String errCode;
    private String message;
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
    private Integer delayTimeTotalTimeCost;
    private List<Object> infoList;
    private JobType jobType;
}
