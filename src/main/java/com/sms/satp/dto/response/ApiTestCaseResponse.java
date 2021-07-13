package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiTestCaseResponse extends BaseResponse {

    private String caseName;
    private String apiId;
    private String projectId;
    private String apiName;
    private String description;
    private String apiPath;
    private Integer apiProtocol;
    private Integer status;
    private Integer requestMethod;
    private Integer apiRequestParamType;
    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> responseHeaders;
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
    private ResponseHeadersVerificationResponse responseHeadersVerificationResponse;
    private ResponseResultVerificationResponse responseResultVerificationResponse;
    private ResponseTimeVerification responseTimeVerification;
    private Boolean execute;
    private Boolean removed;
}
