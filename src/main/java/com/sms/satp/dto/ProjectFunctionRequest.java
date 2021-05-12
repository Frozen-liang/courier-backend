package com.sms.satp.dto;

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
public class ProjectFunctionRequest {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty")
    private String projectId;
    @NotEmpty(message = "The functionKey cannot be empty")
    private String functionKey;
    @NotEmpty(message = "The functionName cannot be empty")
    private String functionName;
    @Valid
    private List<ParamInfoRequest> functionParams;
    private String functionCode;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
