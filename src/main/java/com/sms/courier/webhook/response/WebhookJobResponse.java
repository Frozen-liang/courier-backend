package com.sms.courier.webhook.response;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WebhookJobResponse {

    @Default
    private Integer jobStatus = 2;

    private String message;

    private String errCode;
    @Default
    private Integer totalTimeCost = 0;
    @Default
    private Integer paramsTotalTimeCost = 0;
}
