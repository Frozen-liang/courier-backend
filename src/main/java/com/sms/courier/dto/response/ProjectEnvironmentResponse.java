package com.sms.courier.dto.response;

import com.sms.courier.entity.env.EnvironmentAuth;
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

    private String workspaceId;
    private String projectId;
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
}