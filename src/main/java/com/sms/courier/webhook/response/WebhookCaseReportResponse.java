package com.sms.courier.webhook.response;


import java.util.List;
import java.util.Map;
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

    private Map<String, Object> requestHeader;

    private Map<String, Object> responseHeader;

    private Object responseData;

    private Object requestData;

    private Integer isSuccess;

    private String errCode;

    private String failMessage;

    //The time it takes to run.
    private Integer timeCost;

    // The time it takes to prepare parameters.
    private Integer paramsTimeCost;

    private Integer delayTime;

    private List<Object> infoList;

    private Integer preInjectTimeCost;

    private Integer postInjectTimeCost;

}
