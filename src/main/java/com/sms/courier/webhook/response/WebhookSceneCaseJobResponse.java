package com.sms.courier.webhook.response;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WebhookSceneCaseJobResponse extends WebhookJobResponse {

    @Default
    private String caseReportList = null;
}
