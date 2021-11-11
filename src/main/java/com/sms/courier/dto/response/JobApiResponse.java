package com.sms.courier.dto.response;

import com.sms.courier.common.enums.RawType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApiResponse {

    private String id;

    private String projectId;

    private String apiName;

    private String description;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiRequestParamType;

    private String requestRaw;

    private Integer requestRawType;

    private List<ParamInfoResponse> requestHeaders;

    private List<ParamInfoResponse> responseHeaders;

    private List<ParamInfoResponse> pathParams;

    private List<ParamInfoResponse> restfulParams;

    private List<ParamInfoResponse> requestParams;

    private List<ParamInfoResponse> responseParams;

    private String preInject;

    private String postInject;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private Integer apiResponseParamType;

    private String responseRaw;

    private RawType responseRawType;
}
