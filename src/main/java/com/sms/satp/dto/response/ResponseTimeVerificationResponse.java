package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTimeVerificationResponse {

    @JsonProperty("isCheckStatus")
    private boolean checkStatus;
    private Integer timeoutLimit;
}
