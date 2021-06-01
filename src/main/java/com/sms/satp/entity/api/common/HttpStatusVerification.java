package com.sms.satp.entity.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpStatusVerification {

    private Boolean checkStatus;

    private String statusCode;

    private String verificationResult;
}
