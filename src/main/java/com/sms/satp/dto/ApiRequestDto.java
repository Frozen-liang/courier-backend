package com.sms.satp.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequestDto {

    @NotBlank(message = "The projectId must not be null.")
    private String projectId;

    private Integer groupId;

    private List<Integer> tagId;

    @NotBlank(message = "The apiName must not be null.")
    private String apiName;

    private String description;

    @NotBlank(message = "The apiPath must not be null.")
    private String apiPath;

    @NotBlank(message = "The apiProtocol must not be null.")
    private Integer apiProtocol;

    private Integer requestMethod;

    @NotBlank(message = "The apiRequestParamType must not be null.")
    private Integer apiRequestParamType;


    private List<ParamInfoDto> requestHeaders;
    private List<ParamInfoDto> responseHeaders;
    private List<ParamInfoDto> pathParams;
    private List<ParamInfoDto> restfulParams;
    private List<ParamInfoDto> requestParams;
    private List<ParamInfoDto> responseParams;

    @NotBlank(message = "The apiStatus must not be null.")
    private Integer apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;
}
