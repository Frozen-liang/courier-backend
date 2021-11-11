package com.sms.courier.entity.job.common;

import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.entity.api.common.ParamInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApi {

    private String id;

    private String projectId;

    private String apiName;

    private String description;

    private String apiPath;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private String requestRaw;

    private RawType requestRawType;

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

    private ApiRequestParamType apiResponseParamType;

    private String responseRaw;

    private RawType responseRawType;
}
