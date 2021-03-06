package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestRequest {

    private String envId;
    private String apiId;
    private String workspaceId;
    @JsonProperty("isExecute")
    @Default
    private boolean execute = true;
    private String projectId;
    private String apiName;
    private String description;
    private String apiPath;
    private Integer apiProtocol;
    private Integer requestMethod;
    private Integer apiRequestParamType;
    private String requestRaw;
    private Integer requestRawType;
    private List<ParamInfoRequest> requestHeaders;
    private List<ParamInfoRequest> responseHeaders;
    private List<ParamInfoRequest> pathParams;
    private List<ParamInfoRequest> restfulParams;
    private List<ParamInfoRequest> requestParams;
    private List<ParamInfoRequest> responseParams;
    private String preInject;
    private String postInject;
    private Integer apiResponseJsonType;
    private Integer apiRequestJsonType;
    private Integer apiResponseParamType;
    private String responseRaw;
    private Integer responseRawType;
}
