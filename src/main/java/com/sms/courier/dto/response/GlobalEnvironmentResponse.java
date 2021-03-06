package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.entity.env.EnvironmentAuth;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GlobalEnvironmentResponse extends BaseResponse {

    private String workspaceId;
    private String envName;
    private String envDesc;
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private List<ParamInfoResponse> headers;
    private List<ParamInfoResponse> envVariable;
    private List<ParamInfoResponse> urlParams;
    private List<ParamInfoResponse> requestParams;
    private Integer requestParamType;
    @Default
    @JsonProperty("isGlobal")
    private boolean global = true;
}
