package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sms.courier.utils.DurationToStringSerializer;
import com.sms.courier.utils.StringToDurationSerializer;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockApiResponse {

    private String projectId;

    private String apiId;

    private String mockName;

    private String mockPath;

    /**
     * 根数据生成规则.
     */
    private String rootMockRule;

    private Integer apiResponseParamType;

    private Integer apiRequestParamType;

    private String requestRaw;

    private Integer requestRawType;

    private List<ParamInfoResponse> requestHeaders;

    private List<ParamInfoResponse> responseHeaders;

    private List<ParamInfoResponse> pathParams;

    private List<ParamInfoResponse> restfulParams;

    private List<MatchParamInfoResponse> requestParams;

    private List<ParamInfoResponse> responseParams;

    private String responseRaw;

    private Integer responseRawType;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    /**
     * xml 声明.
     */
    private String xmlDeclaration;

    private String statusCode;

    /**
     * 延期时间，单位：毫秒.
     */
    @JsonSerialize(using = DurationToStringSerializer.class)
    @JsonDeserialize(using = StringToDurationSerializer.class)
    private Duration delayTime;

    @JsonProperty(value = "isEnable")
    private boolean enable;

    private ApiResponse apiEntity;
}
