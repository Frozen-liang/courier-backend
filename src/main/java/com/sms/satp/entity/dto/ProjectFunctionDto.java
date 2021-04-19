package com.sms.satp.entity.dto;

import com.sms.satp.entity.function.FunctionParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectFunctionDto {

    private String id;
    private String projectId;
    private String functionDesc;
    private String functionName;
    private List<FunctionParam> functionParams;
    private String functionCode;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
