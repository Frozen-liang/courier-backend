package com.sms.courier.entity.job.common;

import com.sms.courier.entity.api.common.ParamInfo;
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
public class JobEnvironment {

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

    private List<ParamInfo> headers;

    private List<ParamInfo> envVariable;

    private List<ParamInfo> urlParams;

    private List<ParamInfo> requestParams;

    private Integer requestParamType;

}
