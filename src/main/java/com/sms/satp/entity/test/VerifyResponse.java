package com.sms.satp.entity.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyResponse {

    private Integer statusCode;
    private Boolean enableExecutionTime;
    private Integer executionTime;

}