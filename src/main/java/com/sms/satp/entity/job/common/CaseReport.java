package com.sms.satp.entity.job.common;


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

    private int status;

    private String requestUrl;

    private Map<String, String> requestHeader;

    private Map<String, String> responseHeader;

    private Object responseData;

    private Object requestData;

    private Boolean result;

    private String failMessage;

    private Long runtime;

}
