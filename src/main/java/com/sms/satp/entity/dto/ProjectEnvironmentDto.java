package com.sms.satp.entity.dto;

import com.sms.satp.entity.AuthInfo;
import com.sms.satp.entity.env.EnvironmentAuth;
import com.sms.satp.entity.env.EnvironmentHeader;
import com.sms.satp.entity.env.EnvironmentParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEnvironmentDto {

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
    private String createDateTime;
    private String modifyDateTime;
    private List<EnvironmentHeader> headers;
    private List<EnvironmentParam> params;
    private List<EnvironmentParam> urlParams;
    private List<EnvironmentParam> additionalParams;
}