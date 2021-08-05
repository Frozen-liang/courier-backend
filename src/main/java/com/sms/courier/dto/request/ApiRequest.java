package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id must not be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId must not be null.")
    private String projectId;

    private String groupId;

    private List<String> tagId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiName must not be null.")
    private String apiName;

    private String description;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiPath must not be null.")
    private String apiPath;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiProtocol must not be null.")
    private ApiProtocol apiProtocol;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The requestMethod must not be null.")
    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private String requestRaw;

    private RawType requestRawType;
    @Valid
    private List<ParamInfoRequest> requestHeaders;
    @Valid
    private List<ParamInfoRequest> responseHeaders;
    @Valid
    private List<ParamInfoRequest> pathParams;
    @Valid
    private List<ParamInfoRequest> restfulParams;
    @Valid
    private List<ParamInfoRequest> requestParams;
    @Valid
    private List<ParamInfoRequest> responseParams;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiStatus must not be null.")
    private ApiStatus apiStatus;
    @JsonProperty("isRemoved")
    private boolean removed;

    private String preInject;

    private String postInject;

    //@NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiResponseJsonType must not be null.")
    private ApiJsonType apiResponseJsonType;

    //@NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiRequestJsonType must not be null.")
    private ApiJsonType apiRequestJsonType;

    private ApiRequestParamType apiResponseParamType;

    private String responseRaw;

    private RawType responseRawType;
}
