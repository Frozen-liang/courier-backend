package com.sms.satp.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EnvironmentHeader {

    private String headerName;
    private String headerValue;
}
