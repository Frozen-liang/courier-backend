package com.sms.satp.dto;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.api.common.ParamInfo;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCaseTemplateApiRequest {

    @NotNull(message = "The apiId can not be empty")
    private String apiId;
    @NotNull(message = "The caseTemplateId can not be empty")
    private String caseTemplateId;
    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    @NotNull(message = "The apiName can not be empty")
    private String apiName;

    private String description;
    @NotNull(message = "The apiPath can not be empty")
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

    private String preInject;

    private String postInject;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    private String matchRule;

    private Integer timeoutLimit;

    private Integer orderNumber;

    private Integer isExecute;

    private boolean remove;
}
