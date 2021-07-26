package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HttpStatusVerificationResponse {

    @JsonProperty("isCheckStatus")
    private boolean checkStatus;
    private String statusCode;
}
