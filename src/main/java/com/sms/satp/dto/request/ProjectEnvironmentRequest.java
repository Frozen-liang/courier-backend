package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.entity.env.EnvironmentAuth;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEnvironmentRequest {

    @NotEmpty(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    private String workspaceId;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId must not be empty")
    private String projectId;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The envName must not be empty")
    private String envName;

    private String envDesc;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The frontUri must not be empty")
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