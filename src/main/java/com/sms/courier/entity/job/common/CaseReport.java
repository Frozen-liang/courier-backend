package com.sms.courier.entity.job.common;


import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.enums.ResultType;
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
