package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.HeaderInfo;
import com.sms.satp.entity.env.EnvironmentAuth;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProjectEnvironmentResponse extends BaseResponse {

    private String projectId;
    private String envName;
    private String envDesc;
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private List<HeaderInfo> headers;
    private List<ParamInfoResponse> params;
    private List<ParamInfoResponse> urlParams;
    private List<ParamInfoResponse> additionalParams;
}