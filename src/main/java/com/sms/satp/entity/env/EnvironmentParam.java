package com.sms.satp.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EnvironmentParam {

    private String paramKey;
    private String paramValue;
    private String paramDesc;
}
