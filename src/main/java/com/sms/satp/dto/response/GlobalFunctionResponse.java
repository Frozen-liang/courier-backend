package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GlobalFunctionResponse extends BaseResponse {

    private String functionKey;
    private String functionName;
    private List<ParamInfoResponse> functionParams;
    private String functionCode;
    @Default
    @JsonProperty("isGlobal")
    private boolean global = true;
}
