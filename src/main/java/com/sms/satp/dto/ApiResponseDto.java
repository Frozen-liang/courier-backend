package com.sms.satp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto {

    private String id;

    private String projectId;

    private String group;

    private List<String> tag;

    private String apiName;

    private String description;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiRequestParamType;


    private List<ParamInfoDto> requestHeaders;
    private List<ParamInfoDto> responseHeaders;
    private List<ParamInfoDto> pathParams;
    private List<ParamInfoDto> restfulParams;
    private List<ParamInfoDto> requestParams;
    private List<ParamInfoDto> responseParams;

    private Integer apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    private String swaggerId;


    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private String createUser;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
