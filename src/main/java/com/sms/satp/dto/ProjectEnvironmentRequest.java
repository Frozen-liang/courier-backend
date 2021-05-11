package com.sms.satp.dto;

import com.sms.satp.entity.api.common.HeaderInfo;
import com.sms.satp.entity.env.EnvironmentAuth;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEnvironmentRequest {

    private String id;
    @NotEmpty(message = "The projectId must not be empty")
    private String projectId;
    @NotEmpty(message = "The envName must not be empty")
    private String envName;
    private String envDesc;
    @NotEmpty(message = "The frontUri must not be empty")
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private String createDateTime;
    private String modifyDateTime;
    private String createUserId;
    private String modifyUserId;
    private List<HeaderInfo> headers;
    @Valid
    private List<ParamInfoRequest> params;
    @Valid
    private List<ParamInfoRequest> urlParams;
    @Valid
    private List<ParamInfoRequest> additionalParams;
}