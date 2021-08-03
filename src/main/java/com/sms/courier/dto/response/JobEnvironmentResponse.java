package com.sms.courier.dto.response;

import com.sms.courier.entity.env.EnvironmentAuth;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class JobEnvironmentResponse {

    private String id;

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
