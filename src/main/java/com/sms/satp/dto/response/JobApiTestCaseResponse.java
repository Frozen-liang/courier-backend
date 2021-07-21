package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApiTestCaseResponse {

    private String id;

    private String apiId;

    private String caseName;

    private String projectId;

    private List<String> tagIds;

    private String apiName;

    private String description;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiRequestParamType;

    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> pathParams;
    private List<ParamInfoResponse> restfulParams;
    private List<ParamInfoResponse> requestParams;
    private List<ParamInfoResponse> responseParams;

    private Integer responseParamsExtractionType;

    private String preInject;

    private String postInject;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private HttpStatusVerification httpStatusVerification;

    private ResponseHeadersVerificationResponse responseHeadersVerification;

    private ResponseResultVerificationResponse responseResultVerification;

    private ResponseTimeVerification responseTimeVerification;

    @JsonProperty("isExecute")
    private boolean execute;

    private String modifyUserId;

    private String modifyDateTime;

    private CaseReportResponse caseReport;

}
