package com.sms.satp.entity.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTimeVerification {

    private Boolean checkStatus;

    private Integer timeoutLimit;
}
