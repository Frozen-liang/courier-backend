package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GlobalFunctionResponse extends FunctionResponse {
    private String workspaceId;
    @Default
    @JsonProperty("isGlobal")
    private boolean global = true;
}
