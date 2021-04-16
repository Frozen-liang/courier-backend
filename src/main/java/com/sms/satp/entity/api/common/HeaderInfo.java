package com.sms.satp.entity.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderInfo {

    private String key;
    private String value;
    private String description;
    private boolean required;
}
