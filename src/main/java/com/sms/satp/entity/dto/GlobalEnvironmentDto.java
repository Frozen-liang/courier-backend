package com.sms.satp.entity.dto;

import com.sms.satp.entity.env.EnvironmentAuth;
import com.sms.satp.entity.env.EnvironmentHeader;
import com.sms.satp.entity.env.EnvironmentParam;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalEnvironmentDto {

    private String id;
    @NotEmpty(message = "EvnName cannot be empty")
    private String envName;
    private String envDesc;
    @NotEmpty(message = "FrontUri cannot be empty")
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
