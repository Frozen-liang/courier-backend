package com.sms.courier.webhook.response;

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
public class WebhookSceneCaseJobResponse extends WebhookJobResponse {

    private List<WebhookCaseReportResponse> caseReportList;
}
