package com.sms.satp.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequest {

    private String id;

    @NotBlank(message = "The projectId must not be null.")
    private String projectId;

    private String groupId;

    private List<String> tagId;

    @NotBlank(message = "The apiName must not be null.")
    private String apiName;

    private String description;

    @NotNull(message = "The apiPath must not be null.")
    private String apiPath;

    @NotNull(message = "The apiProtocol must not be null.")
    private Integer apiProtocol;

    @NotNull(message = "The requestMethod must not be null.")
    private Integer requestMethod;

    @NotNull(message = "The apiRequestParamType must not be null.")
    private Integer apiRequestParamType;


    @Valid
    private List<ParamInfoRequest> requestHeaders;
    @Valid
    private List<ParamInfoRequest> responseHeaders;
    @Valid
    private List<ParamInfoRequest> pathParams;
    @Valid
    private List<ParamInfoRequest> restfulParams;
    @Valid
    private List<ParamInfoRequest> requestParams;
    @Valid
    private List<ParamInfoRequest> responseParams;

    @NotNull(message = "The apiStatus must not be null.")
    private Integer apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    @NotNull(message = "The apiResponseJsonType must not be null.")
    private Integer apiResponseJsonType;

    @NotNull(message = "The apiRequestJsonType must not be null.")
    private Integer apiRequestJsonType;
}
