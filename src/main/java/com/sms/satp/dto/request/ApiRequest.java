package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private Integer apiProtocol;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The requestMethod must not be null.")
    private Integer requestMethod;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiRequestParamType must not be null.")
    private Integer apiRequestParamType;


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
    private Integer apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiResponseJsonType must not be null.")
    private Integer apiResponseJsonType;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The apiRequestJsonType must not be null.")
    private Integer apiRequestJsonType;
}
