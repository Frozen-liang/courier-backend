package com.sms.satp.entity.dto;

import com.sms.satp.entity.function.FunctionParam;
import java.time.LocalDateTime;
import java.util.List;
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
    private String functionDesc;
    private String functionName;
    private List<FunctionParam> functionParams;
    private String functionCode;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
}
