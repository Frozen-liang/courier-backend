package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.MockApiJsonLocateType;
import com.sms.courier.common.enums.MockApiResponseParamType;
import com.sms.courier.common.enums.RawType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.utils.DurationToStringSerializer;
import com.sms.courier.utils.StringToDurationSerializer;
import java.time.Duration;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockApiRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id can not be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId can not be empty.")
    private String projectId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiId can not be empty.")
    private String apiId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The mockName can not be empty.")
    private String mockName;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The mockPath can not be empty.")
    private String mockPath;

    /**
     * 根数据生成规则.
     */
    private String rootMockRule;

    private MockApiResponseParamType apiResponseParamType;

    private ApiRequestParamType apiRequestParamType;

    private String requestRaw;

    private RawType requestRawType;

    private List<ParamInfoRequest> requestHeaders;

    private List<ParamInfoRequest> responseHeaders;

    private List<ParamInfoRequest> pathParams;

    private List<ParamInfoRequest> restfulParams;

    private List<MatchParamInfoRequest> requestParams;

    private List<ParamInfoRequest> responseParams;

    private String responseRaw;

    private RawType responseRawType;

    private ApiJsonType apiResponseJsonType;

    private ApiJsonType apiRequestJsonType;

    private MockApiJsonLocateType jsonLocateType;

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

}
