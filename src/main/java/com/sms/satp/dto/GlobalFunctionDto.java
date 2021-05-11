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
public class GlobalFunctionDto {

    private String id;
    @NotEmpty(message = "The functionDesc cannot be empty")
    private String functionDesc;
    @NotEmpty(message = "The functionName cannot be empty")
    private String functionName;
    @Valid
    private List<ParamInfoDto> functionParams;
    private String functionCode;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
