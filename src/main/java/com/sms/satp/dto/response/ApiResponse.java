package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiResponse extends BaseResponse {

    private String projectId;

    private String groupName;

    private List<String> tagName;

    private String apiName;

    private String description;

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

    private Integer apiStatus;

    private String preInject;

    private String postInject;

    private String swaggerId;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private String createUser;
}
