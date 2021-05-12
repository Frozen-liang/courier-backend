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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalEnvironmentRequest {

    private String id;
    @NotEmpty(message = "The evnName cannot be empty")
    private String envName;
    private String envDesc;
    @NotEmpty(message = "The frontUri cannot be empty")
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
    private List<HeaderInfo> headers;
    @Valid
    private List<ParamInfoRequest> params;
    @Valid
    private List<ParamInfoRequest> urlParams;
    @Valid
    private List<ParamInfoRequest> additionalParams;
}
