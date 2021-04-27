package com.sms.satp.entity.dto;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.test.CaseHeader;
import com.sms.satp.entity.test.CaseParameter;
import com.sms.satp.entity.test.CaseRequestBody;
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
    private CaseRequestBody requestBody;
    private List<CaseHeader> requestHeaders;
    private List<CaseParameter> queryParams;
    private List<CaseParameter> pathParams;
    private String preInject;
    private String postInject;
    private ApiRequestParamType apiResponseParamType;
    private List<CaseHeader> responseHeaders;
    private List<CaseParameter> responseParams;
    private String matchRule;
    private Integer orderNumber;
    private Integer isExecute;
    private boolean remove;
    private String createUserId;
    private LocalDateTime createDateTime;
    private String modifyUserId;
    private LocalDateTime modifyDateTime;
}
