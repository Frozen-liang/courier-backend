package com.sms.satp.entity.job.common;


import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.common.enums.ResultType;
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

    private Map<String, String> requestHeader;

    private Map<String, String> responseHeader;

    private Object responseData;

    private Object requestData;

    private ResultType isSuccess;

    private String failMessage;

    private Long timeCost;

}
