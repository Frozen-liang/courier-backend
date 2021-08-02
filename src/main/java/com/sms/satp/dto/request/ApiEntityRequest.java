package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEntityRequest {

    private String id;
    private List<String> tagId;
    private String projectId;
    private String groupId;
    private String apiName;
    private String description;
    private String apiPath;
    private ApiProtocol apiProtocol;
    private RequestMethod requestMethod;
    private ApiRequestParamType apiRequestParamType;
    private ApiRequestParamType apiResponseParamType;
    private List<ParamInfoRequest> requestHeaders;
    private List<ParamInfoRequest> responseHeaders;
    private List<ParamInfoRequest> pathParams;
    private List<ParamInfoRequest> restfulParams;
    private List<ParamInfoRequest> requestParams;
    private List<ParamInfoRequest> responseParams;
    private String preInject;
    private String postInject;
    private ApiJsonType apiResponseJsonType;
    private ApiJsonType apiRequestJsonType;
}
