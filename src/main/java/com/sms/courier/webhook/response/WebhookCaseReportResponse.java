package com.sms.courier.webhook.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookCaseReportResponse {

    @Default
    private Integer status = 500;

    private Integer requestMethod;

    private String requestUrl;

    private String requestHeader;

    private String responseHeader;

    @Default
    private Object responseData = "";

    private String requestData;

    @Default
    private Integer isSuccess = 0;

    private String errCode;

    private String failMessage;

    @Default
    //The time it takes to run.
    private Integer timeCost = 0;

    @Default
    // The time it takes to prepare parameters.
    private Integer paramsTimeCost = 0;

}
