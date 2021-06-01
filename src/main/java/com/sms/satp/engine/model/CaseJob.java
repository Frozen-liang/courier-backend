package com.sms.satp.engine.model;

import com.sms.satp.dto.response.ParamInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseJob {

    private String id;
    private String apiName;
    private String shell;
    private String apiPath;
    private Integer apiProtocol;
    private Integer requestMethod;
    private Integer apiRequestParamType;
    private Integer apiResponseJsonType;
    private Integer apiRequestJsonType;
    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> responseHeaders;
    private List<ParamInfoResponse> pathParams;
    private List<ParamInfoResponse> restfulParams;
    private List<ParamInfoResponse> requestParams;
    private List<ParamInfoResponse> responseParams;
    private String preInject;
    private String postInject;


}
