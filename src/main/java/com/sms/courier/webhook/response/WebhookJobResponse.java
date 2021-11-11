package com.sms.courier.webhook.response;

import com.sms.courier.common.enums.JobStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WebhookJobResponse {
    private JobStatus jobStatus;
    private String message;
    private String errCode;
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
    private Integer delayTimeTotalTimeCost;
    private List<Object> infoList;
}
