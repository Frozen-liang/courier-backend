package com.sms.courier.dto.request;

import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.entity.env.EnvironmentAuth;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalEnvironmentRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The workspaceId cannot be empty")
    private String workspaceId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The evnName cannot be empty")
    private String envName;

    private String envDesc;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The frontUri cannot be empty")
    private String frontUri;

    private EnvironmentAuth envAuth;

    private String beforeInject;

    private String afterInject;

    private String globalBeforeProcess;

    private String globalAfterProcess;

    private List<ParamInfoRequest> headers;

    @Valid
    private List<ParamInfoRequest> envVariable;

    @Valid
    private List<ParamInfoRequest> urlParams;

    private ApiRequestParamType requestParamType;

    @Valid
    private List<ParamInfoRequest> requestParams;
}
