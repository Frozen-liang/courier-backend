package com.sms.satp.entity.dto;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.api.common.ParamInfo;
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
public class SceneCaseApiDto {

    private String id;
    private String apiId;
    private String sceneCaseId;
    private String projectId;
    private String apiName;
    private String apiUrl;
    private ApiProtocol apiProtocol;
    private RequestMethod requestMethod;
    private ApiRequestParamType apiRequestParamType;
    private List<ParamInfo> requestBody;
    private List<ParamInfo> requestHeaders;
    private List<ParamInfo> queryParams;
    private List<ParamInfo> pathParams;
    private String preInject;
    private String postInject;
    private ApiRequestParamType apiResponseParamType;
    private List<ParamInfo> responseHeaders;
    private List<ParamInfo> responseParams;
    private String matchRule;
    private Integer orderNumber;
    private Integer isExecute;
    private boolean remove;
    private String createUserId;
    private LocalDateTime createDateTime;
    private String modifyUserId;
    private LocalDateTime modifyDateTime;
}
