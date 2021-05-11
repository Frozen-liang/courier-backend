package com.sms.satp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String groupName;

    private List<String> tagName;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDateTime;
}
