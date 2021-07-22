package com.sms.satp.dto.response;


import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseReportResponse {

    private String caseId;

    private Integer status;

    private Integer requestMethod;

    private String requestUrl;

    private Map<String, String> requestHeader;

    private Map<String, String> responseHeader;

    private Object responseData;

    private Object requestData;

    private Integer isSuccess;

    private String failMessage;

    private Long timeCost;

}
