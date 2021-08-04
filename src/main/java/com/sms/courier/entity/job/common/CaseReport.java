package com.sms.courier.entity.job.common;


import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.enums.ResultType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseReport {

    private String caseId;

    private Integer status;

    private RequestMethod requestMethod;

    private String requestUrl;

    private Map<String, Object> requestHeader;

    private Map<String, Object> responseHeader;

    private Object responseData;

    private Object requestData;

    private ResultType isSuccess;

    private Integer errCode;

    private String failMessage;

    //The time it takes to run.
    private Integer timeCost;

    // The time it takes to prepare parameters.
    private Integer paramsTimeCost;

}
