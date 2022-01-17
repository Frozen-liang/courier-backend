package com.sms.courier.webhook.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WebhookJobResponse {
    private Integer jobStatus;
    private String message;
    private String errCode;
    private Integer totalTimeCost;
    private Integer paramsTotalTimeCost;
}
