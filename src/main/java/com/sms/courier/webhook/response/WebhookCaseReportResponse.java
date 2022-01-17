package com.sms.courier.webhook.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookCaseReportResponse {

    private Integer status;

    private Integer requestMethod;

    private String requestUrl;

    private String requestHeader;

    private String responseHeader;

    private Object responseData;

    private Object requestData;

    private Integer isSuccess;

    private String errCode;

    private String failMessage;

    //The time it takes to run.
    private Integer timeCost;

    // The time it takes to prepare parameters.
    private Integer paramsTimeCost;

}
