package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.HeaderInfo;
import com.sms.satp.entity.env.EnvironmentAuth;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalEnvironmentResponse {

    private String id;
    private String envName;
    private String envDesc;
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
    private List<ParamInfoResponse> params;
    private List<ParamInfoResponse> urlParams;
    private List<ParamInfoResponse> additionalParams;
}
