package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.common.enums.ResponseParamsExtractionType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ParamInfo;
import com.sms.satp.entity.api.common.ResponseHeadersVerification;
import com.sms.satp.entity.api.common.ResponseResultVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestCaseRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id can not be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The caseName can not be empty.")
    private String caseName;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiId can not be empty.")
    private String apiId;
    private List<String> tagIds;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId can not be empty.")
    private String projectId;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiName can not be empty.")
    private String apiName;
    private String description;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiPath can not be empty.")
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
    private ResponseParamsExtractionType responseParamsExtractionType;
    private String preInject;
    private String postInject;
    private ApiJsonType apiResponseJsonType;
    private ApiJsonType apiRequestJsonType;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerification responseHeadersVerification;
    private ResponseResultVerification responseResultVerification;
    private ResponseTimeVerification responseTimeVerification;
    private String createUserName;
    private String dataCollId;
    @Default
    @JsonProperty("isExecute")
    private boolean execute = true;
}
