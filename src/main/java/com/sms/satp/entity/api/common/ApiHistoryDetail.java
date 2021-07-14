package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.RequestMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryDetail {

    private String projectId;

    private String groupId;

    private List<String> tagId;

    private String apiName;

    private String description;

    private String apiPath;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private List<ParamInfo> requestHeaders;

    private List<ParamInfo> responseHeaders;

    private List<ParamInfo> pathParams;

    private List<ParamInfo> restfulParams;

    private List<ParamInfo> requestParams;
    private List<ParamInfo> responseParams;

    private ApiStatus apiStatus;

    private String preInject;

    private String postInject;

    private String swaggerId;

    private String md5;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;
    private String id;
    private Boolean removed;
    private String createUserId;
    private String modifyUserId;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
}
