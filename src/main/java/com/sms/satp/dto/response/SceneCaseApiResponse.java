package com.sms.satp.dto.response;

import com.sms.satp.common.enums.ApiBindingStatus;
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
public class SceneCaseApiResponse {

    private String id;
    private String apiId;
    private String sceneCaseId;
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
    private Integer orderNumber;
    private Boolean isExecute;
    private Boolean removed;
    private Integer apiBindingStatus;
    private Boolean isLock;
    private Long createUserId;
    private LocalDateTime createDateTime;
    private Long modifyUserId;
    private LocalDateTime modifyDateTime;
}
