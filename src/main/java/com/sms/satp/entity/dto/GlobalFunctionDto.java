package com.sms.satp.entity.dto;

import com.sms.satp.entity.function.FunctionParam;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalFunctionDto {

    private String id;
    @NotEmpty(message = "FunctionDesc cannot be empty")
    private String functionDesc;
    @NotEmpty(message = "FunctionName cannot be empty")
    private String functionName;
    private List<FunctionParam> functionParams;
    private String functionCode;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
