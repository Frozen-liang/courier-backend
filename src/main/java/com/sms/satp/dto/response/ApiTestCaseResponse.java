package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestCaseResponse {

    private String id;
    private String caseName;
    private String apiId;
    private String projectId;
    private String apiName;
    private String description;
    private Integer apiType;
    private String jsData;
    private String apiPath;
    private Integer apiProtocol;
    private Integer requestMethod;
    private Integer apiRequestParamType;
    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> responseHeaders;
    private List<ParamInfoResponse> pathParams;
    private List<ParamInfoResponse> restfulParams;
    private List<ParamInfoResponse> requestParams;
    private List<ParamInfoResponse> responseParams;
    private String preInject;
    private String postInject;
    private Integer apiResponseJsonType;
    private Integer apiRequestJsonType;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerificationResponse responseHeadersVerificationResponse;
    private ResponseResultVerificationResponse responseResultVerificationResponse;
    private ResponseTimeVerification responseTimeVerification;
    private Boolean isExecute;
    private Boolean removed;
    private Long createUserId;
    private String createDateTime;
    private Long modifyUserId;
    private String modifyDateTime;
}
