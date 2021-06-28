package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProjectFunctionResponse extends BaseResponse {

    private String projectId;
    private String workspaceId;
    private String functionKey;
    private String functionName;
    private List<ParamInfoResponse> functionParams;
    private String functionCode;
}
